package com.joow.route

import akka.http.model.StatusCodes
import com.joow.app.Json4sProtocol
import com.joow.entity.{Account, RsHeader, Response, Blog}
import com.joow.service._
import org.json4s.JsonAST.JObject
import spray.http.MediaTypes
import spray.routing.SimpleRoutingApp
import scala.util.{Failure, Success}

/**
 *
 * Created by SAM on 2015/3/21.
 */
object AuthRouting extends SimpleRoutingApp with AuthOperations {

  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  val route_create = {
    path("auth") {
      post {
        entity(as[JObject]) { jsonObj =>
          val authentication = jsonObj.extract[com.joow.entity.Authentication]
          respondWithMediaType(MediaTypes.`application/json`) {
            onComplete(doLogin(authentication.email, authentication.passwd)) {
              case Success(value) => {
                complete {
                  val body: Map[Any, Any] = Map("access_token" -> value)
                  val res = Response(RsHeader("0"), body)
                  res
                }
              }
              case Failure(ex) => {
                complete {
                  val body: Map[Any, Any] = Map("message" -> ex.getMessage)
                  val res = Response(RsHeader("9"), body)
                  res
                }
              }
            }
          }
        }
      }
    }
  }

  val route_get = {
    path("auth" / Segment) { accessToken: String =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            val account: Account = getAccountByToken(accessToken).copy(passwd = "")
            val body: Map[Any, Any] = Map("account" -> account)
            val res = Response(RsHeader("0"), body)
            res
          }
        }
      }
    }
  }
}
