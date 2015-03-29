package com.joow.service

import com.hazelcast.core.IMap
import com.joow.entity.AccessToken
import com.joow.hazelcast.{TokenHZOP, HzHelper}
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.commons.codec.digest.DigestUtils
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.SearchHits

import scala.concurrent.{Future, Promise}
import scala.util.{Success, Failure}

/**
 * Created by SAM on 2015/3/21.
 */
trait AuthOperations {
  //token的加料，以免太好猜被猜到
  val salt = "1533149171bf567efe1e9b93cf8980de"

  import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * 取出帳號資料
   * @param email
   * @return
   */
  def findAccByEmail(email: String): Future[SearchResponse] = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp: Future[SearchResponse] = client.execute {
      search in "joow/account" query matchQuery("email", email)
    }
    resp
  }

  /**
   * 將資料轉換成Token
   * @param searchrs
   * @param passwd
   * @return String
   */
  def getAccessToken(searchrs:Future[SearchResponse], passwd: String): Future[String] = {
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
              TokenHZOP.saveAccessToken(AccessToken(tokenStr, "", email, "web", ""))

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

  /**
   * 驗證密碼是否正確
   * @param inputPasswd
   * @param dbpasswd
   * @return
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
