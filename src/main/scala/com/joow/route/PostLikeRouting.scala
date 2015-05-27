package com.joow.route

import com.joow.app.Json4sProtocol
import com.joow.service.KeywordOperations
import spray.http.{StatusCodes, MediaTypes}
import spray.routing.SimpleRoutingApp
import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/5/25.
 */
object PostLikeRouting extends SimpleRoutingApp with KeywordOperations{
  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_query
  }

  private val route_query = {
    path("postslike") {
      get {
        parameters('access_token.? , 'postsid) { (access_token, postsid) =>
          respondWithMediaType(MediaTypes.`application/json`) {
            onComplete(queryPostsLike(postsid)) {
              case Success(value) => {
                complete(StatusCodes.OK, value)
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
}
