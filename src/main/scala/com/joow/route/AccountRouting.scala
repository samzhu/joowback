package com.joow.route

import java.io.StringWriter
import java.util
import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.joow.app.Json4sProtocol
import com.joow.entity._
import com.joow.utils.Print
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.commons.codec.digest.DigestUtils
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.{SearchHits, SearchHit}
import org.elasticsearch.search.internal.InternalSearchHit
import org.json4s.{Extraction, DefaultFormats, Formats}
import spray.http.MediaTypes
import spray.httpx.Json4sSupport
import spray.routing.{SimpleRoutingApp, HttpService, Directives, Route}
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable.ListBuffer





object AccountRouting extends SimpleRoutingApp {

  import Json4sProtocol._

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
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              val account = accountObj.extract[Account]
              val saveaccount = new Account(account.email, DigestUtils.sha512Hex(account.passwd), account.nickname, account.createDate)
              println("密碼:"+saveaccount.passwd)
              val client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                index into "joow/account" doc saveaccount
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
            val account = Account(source.get("email").toString, "", source.get("nickname").toString, None)
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
              resp.getHits.getHits().foreach( u =>
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
