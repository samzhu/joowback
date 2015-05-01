package com.joow.service

import com.joow.elastic.{AccountEs, ProfileEs}
import com.joow.entity.{Profile, Account}
import com.joow.fromdata.FormCreateAccount
import com.joow.hazelcast.AccountHz
import com.joow.utils.UtilTime
import org.apache.commons.codec.digest.DigestUtils

import scala.collection.mutable
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/3/30.
 */
trait AccountOperations extends AccountHz with AccountEs with ProfileEs {

  import scala.concurrent.ExecutionContext.Implicits.global

  def createAccount(formaccount: FormCreateAccount): Future[String] = {
    val promise = Promise[String]()
    //將用戶密碼雜湊處理
    val saveaccount: Account = createAccountToHz(Account("", formaccount.email, DigestUtils.sha512Hex(formaccount.passwd)))
    println(saveaccount)
    saveAccountToEs(saveaccount)
    val profile: Profile = Profile(saveaccount.userid, formaccount.nickname, null, mutable.Buffer(), mutable.Buffer(), UtilTime.getUTCTime())
    val resp = saveProfileToEs(profile)
    Future {
      try {
        resp onComplete {
          case Success(result) => {
            promise.success(saveaccount.userid)
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
    //modifyAccountToHz(account)
  }

}
