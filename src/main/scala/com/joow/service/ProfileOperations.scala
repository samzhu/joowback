package com.joow.service

import com.joow.elastic.ProfileEs
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/4/11.
 */
trait ProfileOperations extends ProfileEs {
  import scala.concurrent.ExecutionContext.Implicits.global
  import com.joow.entity.Profile
  import org.elasticsearch.action.get.GetResponse

  protected def getProfile(access_token: String, userid: String): Future[Option[Profile]] = {
    val promise = Promise[Option[Profile]]()
    val resp: Future[GetResponse] = getProfileEs(userid)
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
