package com.joow.route

import java.io.StringWriter
import java.util
import akka.actor.ActorSystem
import akka.http.model.StatusCode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.joow.app.Json4sProtocol
import com.joow.entity._
import com.joow.route.AuthRouting._
import com.joow.service.AccountOperations
import com.joow.utils.Print
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.ElasticDsl.delete
import com.sksamuel.elastic4s.ElasticDsl.get
import com.sksamuel.elastic4s.ElasticDsl.put
import org.apache.commons.codec.digest.DigestUtils
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.{SearchHits, SearchHit}
import org.elasticsearch.search.internal.InternalSearchHit
import org.json4s.{Extraction, DefaultFormats, Formats}
import spray.http.{StatusCodes, HttpResponse, MediaTypes}
import spray.httpx.Json4sSupport
import spray.routing.{SimpleRoutingApp, HttpService, Directives, Route}
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success}

object AccountRouting extends SimpleRoutingApp with AccountOperations {

  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  // single node
  //var client: ElasticClient = null

  lazy val route = {
    funcreate ~ fundelete ~ funedit ~ funget ~ funquery
  }

  /**
   * 新增
   */
  val funcreate = {
    path("account") {
      post {
        entity(as[JObject]) { accountObj =>
          val account = accountObj.extract[Account]
          respondWithMediaType(MediaTypes.`application/json`) {
            //將用戶密碼雜湊處理
            val saveaccount: Account = account.copy(passwd = DigestUtils.sha512Hex(account.passwd))
            onComplete(createAccount(saveaccount)) {
              case Success(value) => {
                complete(StatusCodes.Created, Map("msg" -> value))
              }
              case Failure(ex) => {
                complete(StatusCodes.InternalServerError, Map("msg" -> ex.getMessage))
              }
            }
          }
        }
      }
    }
  }

  /**
   * 刪除
   */
  val fundelete = {
    path("account" / Segment) { accountid =>
      delete {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            val client = ElasticClient.remote("127.0.0.1", 9300)
            val resp = client.execute {
              com.sksamuel.elastic4s.ElasticDsl.delete id accountid from "joow/account"
            }.await
            client.close()
            val res = Response(RsHeader("0"), Map())
            res
          }
        }
      }
    }
  }

  /**
   * 修改
   */
  val funedit = {
    path("account" / Segment) { accountid =>
      put {
        entity(as[JObject]) { accountObj =>
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              val account = accountObj.extract[Account]
              modifyAccount(account)
              val client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                update id accountid in "joow/account" docAsUpsert(
                  "email" -> account.email,
                  "nickname" -> account.nickname
                  )
              }.await
              client.close()
              val body: Map[Any, Any] = Map("_id" -> resp.getId.toString)
              val res = Response(RsHeader("0"), body)
              res
            }
          }
        }
      }
    }
  }

  /**
   * 取得
   */
  val funget = {
    path("account" / Segment) { accountid =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            val client = ElasticClient.remote("127.0.0.1", 9300)
            val resp = client.execute {
              com.sksamuel.elastic4s.ElasticDsl.get id accountid from "joow/account"
            }.await
            client.close()
            val source = resp.getSource
            val account = Account(Option(""),source.get("email").toString, "", source.get("nickname").toString, None)
            val body: Map[Any, Any] = Map("account" -> account)
            val res = Response(RsHeader("0"), body)
            res
          }
        }
      }
    }
  }

  /**
   * 查詢
   */
  val funquery = {
    path("account") {
      get {
        parameter("nickname") { nickname =>
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              val client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                search in "joow/account" query matchQuery("nickname", nickname)
              }.await
              client.close()
              val sh: SearchHits = resp.getHits
              val hits: ListBuffer[Any] = new ListBuffer()
              resp.getHits.getHits().foreach(u =>
                hits += Map("_id" -> u.getId, "_score" -> u.getScore, "account" -> Account(u))
              )
              val body: Map[Any, Any] = Map("total" -> sh.getTotalHits, "max_score" -> sh.getMaxScore, "hits" -> hits)
              val res = Response(RsHeader("0"), body)
              res
            }
          }
        }
      }
    }
  }
}
