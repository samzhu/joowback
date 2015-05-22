package com.joow.elastic

import com.joow.entity.Account
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl.index
import org.elasticsearch.action.index.IndexResponse

import scala.concurrent.Future

/**
 * Created by SAM on 2015/5/22.
 */
trait KeywordEs {
  private val type_name = "joow/keywordmap"

  protected def saveKeywordToEs(postid:String): Unit ={
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse]  = client.execute {
      index into type_name id account.userid doc account
    }
  }
}
