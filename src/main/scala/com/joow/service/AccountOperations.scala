package com.joow.service

import com.joow.elastic.ProfileEs
import com.joow.entity.{Profile, Account}
import com.joow.hazelcast.AccountHz

import scala.collection.mutable
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/3/30.
 */
trait AccountOperations extends AccountHz with ProfileEs {

  import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * 建立帳號
   * @param account
   * @return
   */
  def createAccount(account: Account): Future[String] = {
    val promise = Promise[String]()
    val saveaccount = createAccountToHz(account)
    val profile: Profile = Profile(saveaccount.userid.get, saveaccount.nickname, null, mutable.Buffer(), mutable.Buffer())
    val resp = saveProfileToEs(profile)
    Future {
      try {
        resp onComplete {
          case Success(result) => {
            promise.success(saveaccount.userid.get)
          }
          case Failure(failure) => {
            promise.failure(failure)
          }
        }
      } catch {
        case ex: Exception => promise.failure(new Exception(ex.getMessage))
      }
    }
    promise.future
    /*暫時先不寫到ES
    val client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                index into "joow/account" doc saveaccount
              }.await
              client.close()
     */
  }

  def modifyAccount(account: Account): Unit = {
    modifyAccountToHz(account)
  }

}
