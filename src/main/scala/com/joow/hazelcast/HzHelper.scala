package com.joow.hazelcast

import com.hazelcast.config.{MapIndexConfig, InMemoryFormat, MapConfig}
import com.hazelcast.core.IMap

/**
 * Created by SAM on 2015/3/29.
 */
object HzHelper extends App{
  import com.hazelcast.config.Config
  import com.hazelcast.core.{Hazelcast, HazelcastInstance}
  var hz:HazelcastInstance = null

  initInstance()

  val token = hz.getMap[String,Object]("token")

  token.put("","")


  /**
   * 初始化
   */
  def initInstance(): Unit ={
    val config:Config = new Config()
    hz = Hazelcast.newHazelcastInstance(config)

    //帳號表格設定
    val accountConfig:MapConfig = config.getMapConfig("normalMap");
    accountConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
    //針對email做index
    accountConfig.addMapIndexConfig(new MapIndexConfig("email", true));

  }

  def getInstance(): HazelcastInstance ={
    hz
  }

  /**
   * 取得特定表格
   * @param mapName
   * @return
   */
  def getMap(mapName:String): IMap ={
     val imap = hz.getMap(mapName)
    imap
  }
}
