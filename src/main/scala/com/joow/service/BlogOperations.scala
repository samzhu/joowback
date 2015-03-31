package com.joow.service

import com.joow.entity.Blog
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.index.IndexResponse

import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
 * Blog Operation Service
 * Created by SAM on 2015/3/21.
 */
trait BlogOperations {

  import scala.concurrent.ExecutionContext.Implicits.global

  def createBlog(accessToken: String, blog: Blog): Unit = {


    /*
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[IndexResponse] = client.execute {
      index into "joow/blog" doc blog
    }
    resp onComplete {
      case Success(result) => println("成功")
      case Failure(failure) => println("失敗")
    }
    */
  }
}
