package com.joow.elastic

import com.joow.entity.Posts
import org.elasticsearch.search.SearchHits

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/5/22.
 */
trait KeywordEs {
  import com.sksamuel.elastic4s.ElasticClient
  import org.elasticsearch.action.search.SearchResponse
  import com.sksamuel.elastic4s.ElasticDsl._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val type_name = "joow/posts"

  protected def queryPostsLikeEs(keywordlist: Seq[String]): Future[Map[String, Any]] = {
    println("keywordlist = " + keywordlist)
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[SearchResponse] = client.execute {
      search in type_name limit 5 query {
        bool {
          must(
            matchQuery("title" , keywordlist) boost 3,
            matchQuery("content" , keywordlist) boost 1,
            matchQuery("tags" , keywordlist) boost 5
          )
        }
      }
    }
    val promise = Promise[Map[String, Any]]()
    resp onComplete {
      case Success(result) => {
        println("Success result = " + result)
        val list: Map[String, Any] = Posts.fromSearchHit(result.getHits)
        promise.success(list)
      }
      case Failure(failure) => {
        promise.failure(failure)
      }
    }
    promise.future
  }
}
