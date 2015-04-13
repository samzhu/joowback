package com.joow.elastic

import scala.concurrent.Future

/**
 * Created by SAM on 2015/4/3.
 */
trait PhotosEs {
  import com.joow.entity.Photo
  import org.elasticsearch.action.get.GetResponse
  import org.elasticsearch.action.index.IndexResponse
  import com.sksamuel.elastic4s.ElasticClient
  import com.sksamuel.elastic4s.ElasticDsl._

  private val type_name = "joow/photos"

  protected def savePhotoEs(photo: Photo): Future[IndexResponse] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse] = client.execute {
      index into type_name id photo.photoid doc photo
    }
    resp
  }

  protected def getPhotoEs(photoid: String): Future[GetResponse] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[GetResponse] = client.execute {
      get id photoid from type_name
    }
    resp
  }
}
