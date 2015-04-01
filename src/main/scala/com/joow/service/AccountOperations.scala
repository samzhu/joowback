package com.joow.service

import com.joow.entity.Account
import com.joow.hazelcast.AccountHz

import scala.concurrent.{Future, Promise}

/**
 * Created by SAM on 2015/3/30.
 */
trait AccountOperations extends AccountHz {

  import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * 建立帳號
   * @param account
   * @return
   */
  def createAccount(account: Account): Future[String] = {
    val promise = Promise[String]()
    Future {
      try {
        val saveaccount = createAccountToHz(account)
        promise.success(saveaccount.userid.get)
      }
      catch {
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
