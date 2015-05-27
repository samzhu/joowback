package com.joow.elastic

import com.joow.entity.Posts
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.search.SearchHits

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/5/25.
 */
trait PostsEs {

  import com.sksamuel.elastic4s.ElasticClient
  import com.sksamuel.elastic4s.ElasticDsl._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val type_name = "joow/posts"

  protected def getPostsByID(postsid: String): Future[Posts] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp:Future[GetResponse] = client.execute {
      get id postsid from type_name
    }
    val promise = Promise[Posts]()
    Future {
      resp onComplete {
        case Success(result) => {
          promise.success(Posts(result.getSourceAsMap))
        }
        case Failure(failure) => {
          promise.failure(failure)
        }
      }
    }
    promise.future
  }
}
