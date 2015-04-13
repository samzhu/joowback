package com.joow.service

import com.joow.entity.{Account, Posts}
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.SearchHits

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}


/**
 * Blog Operation Service
 * Created by SAM on 2015/3/21.
 */
trait PostsOperations extends AuthOperations {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val type_name = "joow/posts"

  /**
   * create writings
   * @param accessToken
   * @param post
   * @return Future[String] => blogid
   */
  def createPost(accessToken: String, post: Posts): Future[String] = {
    val account: Account = getAccountByToken(accessToken)
    val savepost = post.copy(ownerid = account.userid)
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse] = client.execute {
      index into type_name doc savepost
    }
    val promise = Promise[String]()
    Future {
      resp onComplete {
        case Success(result) => {
          promise.success(result.getId)
        }
        case Failure(failure) => {
          println(failure)
          promise.failure(failure)
        }
      }
    }
    promise.future
  }

  def queryPosts(accessToken: String): Future[Map[Any, Any]] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[SearchResponse] = client.execute {
      search in type_name
    }
    val promise = Promise[Map[Any, Any]]()
    Future {
      resp onComplete {
        case Success(result) => {
          val sh: SearchHits = result.getHits
          val hits: ListBuffer[Any] = new ListBuffer()
          result.getHits.getHits().foreach(u =>
            hits += Map("_id" -> u.getId, "_score" -> u.getScore, "post" -> Posts(u))
          )
          val list: Map[Any, Any] = Map("total" -> sh.getTotalHits, "max_score" -> sh.getMaxScore, "hits" -> hits)
          promise.success(list)
        }
        case Failure(failure) => {
          promise.failure(failure)
        }
      }
    }
    promise.future
  }
}
