package com.joow.service

import java.util

import com.joow.elastic.ProfileEs
import com.joow.entity.{Posts, Profile}
import org.ansj.app.keyword.{Keyword, KeyWordComputer}
import org.elasticsearch.action.get.GetResponse

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/5/22.
 */
trait KeywordOperations extends ProfileEs {


  protected def saveKeyword(post: Posts): Future[Option[Profile]] = {

    val kwc: KeyWordComputer = new KeyWordComputer(10)
    val result: util.Collection[Keyword] = kwc.computeArticleTfidf(post.title, post.content)

    val hits: ListBuffer[Any] = new ListBuffer()

    val promise = Promise[Option[Profile]]()
    val resp: Future[GetResponse] = getProfileEs("")
    Future {
      resp onComplete {
        case Success(result) => {
          promise.success(Profile(result.getSourceAsMap))
        }
        case Failure(failure) => {
          promise.failure(failure)
        }
      }
    }
    promise.future
  }
}
