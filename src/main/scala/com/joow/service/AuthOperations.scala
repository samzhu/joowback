package com.joow.service

import com.joow.entity.{Account, AccessToken}
import com.joow.hazelcast.{AccountHz, TokenHz}
import com.joow.utils.UtilTime
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.commons.codec.digest.DigestUtils
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.SearchHits
import scala.concurrent.{Future, Promise}


/**
 * Created by SAM on 2015/3/21.
 */
trait AuthOperations extends TokenHz with AccountHz {
  //token的加料，以免太好猜被猜到
  val salt = DigestUtils.md5Hex("joow")

  import scala.concurrent.ExecutionContext.Implicits.global

  def doLogin(email: String, passwd: String): Future[String] = {
    val promise = Promise[String]()
    Future {
      val account: Account = getAccountByEmail(email)
      if (account == null) {
        promise.failure(new Exception("帳號錯誤"))
      } else {
        val validateresult = validatePasswd(passwd, account.passwd)
        if (validateresult == true) {
          val currentTime = System.currentTimeMillis()
          val tokenStr = DigestUtils.sha512Hex(DigestUtils.md5Hex(email + salt + currentTime) + DigestUtils.md5Hex(salt + currentTime))
          saveAccessTokenHz(AccessToken(tokenStr, "", email, "web", UtilTime.getUTCTime()))
          promise.success(tokenStr)
        } else {
          promise.failure(new Exception("密碼錯誤"))
        }
      }
    }
    promise.future
  }

  def getAccountByToken(token: String): Account = {
    val accessToken: AccessToken = getAccessTokenHz(token)
    val account: Account = getAccountByEmail(accessToken.email)
    //val account: Account = Account(Option("1"),"","","",Option(""))
    account
  }

  /*
  def getAccessTokenForLogin(email:String, passwd: String): Future[String] = {
    val promise = Promise[String]()
    Future {
      searchrs onComplete {
        case Success(result) => {
          val sh: SearchHits = result.getHits
          if (sh.getHits.length == 1) {
            val email: String = sh.getHits()(0).sourceAsMap.get("email").toString
            val dbpasswd = sh.getHits()(0).sourceAsMap.get("passwd").toString
            val validateresult = validatePasswd(passwd, dbpasswd)
            if (validateresult == true) {
              val currentTime = System.currentTimeMillis()
              val tokenStr = DigestUtils.sha512Hex(DigestUtils.md5Hex(email + salt + currentTime) + DigestUtils.md5Hex(salt + currentTime))
              saveAccessToken(AccessToken(tokenStr, "", email, "web", ""))
              promise.success(tokenStr)
            } else {
              promise.failure(new Exception("密碼錯誤"))
            }
          } else {
            promise.failure(new Exception("帳號錯誤"))
          }
        }
        case Failure(failure) => {
          promise.failure(new Exception("連線失敗"))
        }
      }
    }
    promise.future
  }
  */
  /**
   * 驗證密碼是否正確
   * @param inputPasswd
   * @param dbpasswd
   * @return true 密碼正確
   */
  def validatePasswd(inputPasswd: String, dbpasswd: String): Boolean = {
    var validate: Boolean = false
    try {
      if (dbpasswd.equals(DigestUtils.sha512Hex(inputPasswd))) {
        validate = true
      }
    } finally {
    }
    validate
  }
}
