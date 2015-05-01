package com.joow.hazelcast

import com.hazelcast.config.MapStoreConfig
import com.hazelcast.core.HazelcastInstance
import com.sksamuel.elastic4s.ElasticClient
import org.elasticsearch.search.SearchHit

/**
 * Created by SAM on 2015/3/29.
 */
object HzHelper {
  var hz: HazelcastInstance = null
}

trait HzHelper {

  import com.hazelcast.config.{Config, MapIndexConfig, InMemoryFormat, MapConfig}
  import com.hazelcast.core.{Hazelcast, IMap}

  /**
   *
   */
  def initInstance(): Unit = {
    val config: Config = new Config()
    HzHelper.hz = Hazelcast.newHazelcastInstance(config)

    //
    val accountConfig: MapConfig = config.getMapConfig("account");
    accountConfig.setInMemoryFormat(InMemoryFormat.BINARY);
    accountConfig.addMapIndexConfig(new MapIndexConfig("email", true));

    val serialnumberConfig: MapConfig = config.getMapConfig("serialnumber");
    serialnumberConfig.setInMemoryFormat(InMemoryFormat.BINARY);


    /*資料批次抄寫的方法*/
    val serialnumberMapStore: SerialnumberMapStore = new SerialnumberMapStore
    val mapStoreConfig: MapStoreConfig = new MapStoreConfig
    mapStoreConfig.setImplementation(serialnumberMapStore)
    mapStoreConfig.setWriteDelaySeconds(1)
    mapStoreConfig.setWriteBatchSize(1)
    serialnumberConfig.setMapStoreConfig(mapStoreConfig)

    //從ES載入帳號資料到Hz
    loadToHz()

  }

  private def loadToHz(): Unit = {
    import com.sksamuel.elastic4s.ElasticClient
    val client = ElasticClient.remote("127.0.0.1", 9300)
    loadSerialnumberToHz(client)
    loadAccountToHz(client)
  }

  private def loadSerialnumberToHz(client: ElasticClient): Unit = {
    import com.sksamuel.elastic4s.ElasticDsl._
    import org.elasticsearch.action.search.SearchResponse
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: SearchResponse = client.execute {
      search in "joow/serialnumber"
    }.await
    val serialnumberMap: IMap[String, BigInt] = getInstance().getMap[String, BigInt]("serialnumber")
    for (searchHit <- resp.getHits.getHits().toList) {
      val source = searchHit.getSource
      serialnumberMap.put(source.get("key").toString, BigInt(source.get("value").toString))
    }
  }

  private def loadAccountToHz(client: ElasticClient): Unit = {
    import com.joow.entity.Account
    import com.sksamuel.elastic4s.ElasticClient
    import com.sksamuel.elastic4s.ElasticDsl._
    import org.elasticsearch.action.search.SearchResponse
    import scala.collection.JavaConversions._
    import scala.collection.mutable.ListBuffer
    val resp: SearchResponse = client.execute {
      search in "joow/account"
    }.await
    val hits: ListBuffer[Account] = new ListBuffer()
    resp.getHits.getHits().foreach(u =>
      hits += Account(u).get
    )
    val accountMap: IMap[String, Account] = getInstance().getMap[String, Account]("account")
    hits.foreach(a =>
      accountMap.put(a.email, a)
    )
  }

  def getInstance(): HazelcastInstance = {
    HzHelper.hz
  }

  def getMap(mapName: String): IMap[String, Object] = {
    HzHelper.hz.getMap[String, Object](mapName)
  }

}
