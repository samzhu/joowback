package com.joow.elastic

import org.elasticsearch.action.get.GetResponse

import scala.concurrent.Future

/**
 * Created by SAM on 2015/4/11.
 */
trait ProfileEs {

  import com.joow.entity.Profile
  import com.sksamuel.elastic4s.ElasticClient
  import com.sksamuel.elastic4s.ElasticDsl._
  import org.elasticsearch.action.index.IndexResponse

  private val type_name = "joow/profile"

  protected def saveProfileToEs(profile: Profile): Future[IndexResponse] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse] = client.execute {
      index into type_name id profile.userid doc profile
    }
    resp
  }

  protected def getProfileEs(userid: String): Future[GetResponse] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[GetResponse] = client.execute {
      get id userid from type_name
    }
    resp
  }

}
