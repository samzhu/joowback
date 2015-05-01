package com.joow.hazelcast

import java.util
import com.hazelcast.core.MapStore

/**
 * Created by SAM on 2015/4/17.
 */
class SerialnumberMapStore extends MapStore[String, BigInt] {
  override def deleteAll(collection: util.Collection[String]): Unit = {
    //println("AccountMapStore.deleteAll")
  }

  override def store(k: String, v: BigInt): Unit = {
    //向map.put時觸發
    import com.sksamuel.elastic4s.ElasticDsl._
    import com.sksamuel.elastic4s._
    println("AccountMapStore.store k=" + k + ",v=" + v)
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp = client.execute {
      index into "joow/serialnumber" id k fields (
        "key" -> k,
        "value" -> v.toString()
        )
    }.await
  }

  override def delete(k: String): Unit = {
    println("AccountMapStore.delete")
  }

  override def storeAll(map: util.Map[String, BigInt]): Unit = {
    println("AccountMapStore.storeAll")
  }

  override def loadAll(collection: util.Collection[String]): util.Map[String, BigInt] = {
    println("AccountMapStore.loadAll")
    null
  }

  override def loadAllKeys(): util.Set[String] = {
    println("AccountMapStore.loadAllKeys")
    null
  }

  override def load(k: String): BigInt = {
    //每一次使用會被呼叫
    println("AccountMapStore.load")
    null
  }
}
