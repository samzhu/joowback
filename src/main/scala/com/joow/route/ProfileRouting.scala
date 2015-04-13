package com.joow.route


import com.joow.app.Json4sProtocol
import com.joow.service.ProfileOperations
import spray.http.{StatusCodes, MediaTypes}
import spray.routing.SimpleRoutingApp

import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/4/11.
 */
object ProfileRouting extends SimpleRoutingApp with ProfileOperations {

  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_get
  }

  private val route_get = {
    path("profile" / Segment) { userid: String =>
      get {
        parameters('access_token) { access_token =>
          respondWithMediaType(MediaTypes.`application/json`) {
            onComplete(getProfile(access_token, userid)) {
              case Success(value) => {
                complete {
                  StatusCodes.OK -> value.get
                }
              }
              case Failure(ex) => {
                complete {
                  StatusCodes.InternalServerError -> Map("msg" -> ex.getMessage)
                }
              }
            }
          }
        }
      }
    }
  }
}
