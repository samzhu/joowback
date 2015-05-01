package com.joow.hazelcast

import java.util

import com.hazelcast.core.{IMap, MapStore}
import com.joow.entity.Account

import org.elasticsearch.action.search.SearchResponse

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

/**
 * Created by SAM on 2015/4/17.
 */
class AccountMapStore extends MapStore[String, Account] with AccountHz {
  override def deleteAll(collection: util.Collection[String]): Unit = {
    //println("AccountMapStore.deleteAll")
  }

  override def store(k: String, v: Account): Unit = {
    //向map.put時觸發
    import com.sksamuel.elastic4s._
    import com.sksamuel.elastic4s.ElasticDsl._
    println("AccountMapStore.store k=" + k + ",v=" + v)
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp = client.execute {
      index into "joow/account" id v.userid doc v
    }.await
  }

  override def delete(k: String): Unit = {
    //println("AccountMapStore.delete")
  }

  override def storeAll(map: util.Map[String, Account]): Unit = {
    //println("AccountMapStore.storeAll")
  }

  override def loadAll(collection: util.Collection[String]): util.Map[String, Account] = {
    //println("AccountMapStore.loadAll")
    null
  }

  override def loadAllKeys(): util.Set[String] = {
    //println("AccountMapStore.loadAllKeys")
    null
  }

  override def load(k: String): Account = {
    //每一次使用會被呼叫
    //println("AccountMapStore.load")
    null
  }
}
