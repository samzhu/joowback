package com.joow.route

import java.io.StringWriter
import java.util
import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.joow.entity._
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.json4s.{Extraction, DefaultFormats, Formats}
import spray.http.MediaTypes
import spray.httpx.Json4sSupport
import spray.routing.{SimpleRoutingApp, HttpService, Directives, Route}
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._


object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}


object AccountRouting extends SimpleRoutingApp {

  import Json4sProtocol._

  // single node
  var client: ElasticClient = null

  lazy val route = {
    path("account") {
      post {
        entity(as[JObject]) { accountObj =>
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              println("進來POST")
              val account = accountObj.extract[Account]
              client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                index into "joow/account" doc account
              }.await
              client.close()
              val body:Map[Any,Any] = Map("id" -> resp.getId.toString )
              val res = Response( RsHeader("0"), body)
              res
            }
          }
        }
      }
    } ~
    path("account" / Segment) { accountid =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            //println("get accountid=" + accountid)
            client = ElasticClient.remote("127.0.0.1", 9300)
            val resp = client.execute {
              com.sksamuel.elastic4s.ElasticDsl.get id accountid from "joow/account"
            }.await
            client.close()
            val source = resp.getSource
            val account = Account(source.get("email").toString, "", source.get("nickname").toString, "")
            account
          }
        }
      }
    }
  }

  lazy val AccountRoute = {
    path("account") {
      post {
        entity(as[JObject]) { accountObj =>
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              val account = accountObj.extract[Account]
              client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                index into "joow2015/user" doc account
              }.await
              client.close()

              //驗證結果
              val out = new StringWriter
              mapper.writeValue(out, resp)
              println(out.toString)
              out.toString
            }
          }
        }
      }
    }

    //查詢
    path("account") {
      get {
        entity(as[JObject]) { jsonObj =>
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              val AccountQuery = jsonObj.extract[AccountQuery]

              client = ElasticClient.remote("127.0.0.1", 9300)
              val resp = client.execute {
                search in "joow2015/account" -> "nickname" query "小"
              }.await
              client.close()

              resp
            }
          }
        }
      }
    }
    //取得單一筆資料

  }
}
