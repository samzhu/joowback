package com.joow.service

import java.util

import com.joow.elastic.{PostsEs, KeywordEs, ProfileEs}
import com.joow.entity.{Posts, Profile}
import org.ansj.app.keyword.{Keyword, KeyWordComputer}
import org.elasticsearch.action.get.GetResponse

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/5/22.
 */
trait KeywordOperations extends KeywordEs with PostsEs {

  import scala.collection.JavaConversions._
  import scala.concurrent.ExecutionContext.Implicits.global

  protected def queryPostsLike(postsid: String): Future[Map[String, Any]] = {
    val promise = Promise[Map[String, Any]]()
    println("postsid = " + postsid)
    val getPosts: Future[Posts] = getPostsByID(postsid)
    getPosts.map { posts =>
      println("posts = " + posts)
      val keywordhits: ListBuffer[String] = new ListBuffer()
      val kwc: KeyWordComputer = new KeyWordComputer(10)
      val keywordResult: util.Collection[Keyword] = kwc.computeArticleTfidf(posts.title, posts.content)
      println("keywordResult = " + keywordResult)
      for (keyword <- keywordResult) {
        keywordhits += keyword.getName
      }
      for (tag <- posts.tags) {
        keywordhits += tag
      }
      println("keywordhits = " + keywordhits)
      queryPostsLikeEs(keywordhits).map { map =>
        println("mp = " + map)
        promise.success(map)
      }
    }
    promise.future
  }
}
