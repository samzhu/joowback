package com.joow.route

import com.joow.app.Json4sProtocol
import com.joow.entity.Account
import com.joow.rqrsdata.{RsHeader, Response}
import com.joow.service._
import org.json4s.JsonAST.JObject
import spray.http.{StatusCodes, MediaTypes}
import spray.routing.SimpleRoutingApp
import scala.util.{Failure, Success}

/**
 *
 * Created by SAM on 2015/3/21.
 */
object AuthRouting extends SimpleRoutingApp with AuthOperations {

  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_create
  }

  val route_create = {
    path("auth") {
      post {
        entity(as[JObject]) { jsonObj =>
          val authentication = jsonObj.extract[com.joow.entity.Authentication]
          respondWithMediaType(MediaTypes.`application/json`) {
            onComplete(doLogin(authentication.email, authentication.passwd)) {
              case Success(value) => {
                complete(StatusCodes.Created, Map("access_token" -> value))
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
