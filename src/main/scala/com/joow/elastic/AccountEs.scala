package com.joow.elastic

import com.joow.entity.Account
import org.elasticsearch.action.search.SearchResponse

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

/**
 * Created by SAM on 2015/5/2.
 */
trait AccountEs {

  import com.joow.entity.Photo
  import org.elasticsearch.action.get.GetResponse
  import org.elasticsearch.action.index.IndexResponse
  import com.sksamuel.elastic4s.ElasticClient
  import com.sksamuel.elastic4s.ElasticDsl._

  private val type_name = "joow/account"

  protected def saveAccountToEs(account:Account): Unit ={
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse]  = client.execute {
      index into type_name id account.userid doc account
    }
  }
}
