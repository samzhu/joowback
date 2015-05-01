package com.joow.route

import com.joow.app.Json4sProtocol
import com.joow.entity.Posts
import com.joow.route.AccountRouting._
import com.joow.service.PostsOperations
import org.json4s.{DefaultFormats, Formats}
import org.json4s.JsonAST.JObject
import spray.http.{StatusCodes, MediaTypes}
import spray.httpx.Json4sSupport
import spray.routing.SimpleRoutingApp

import scala.util.{Failure, Success}

/**
 * Created by SAM on 2015/3/21.
 */

object PostsRouting extends SimpleRoutingApp with PostsOperations {

  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_create ~ route_query
  }

  /*
  POST graph.facebook.com
  /{user-id}/feed?
    message={message}&
    access_token={access-token}
   */
  private val route_create = {
    path("posts") {
      post {
        parameters('access_token) { (access_token) =>
          entity(as[JObject]) { jsonObj =>
            val post = jsonObj.extract[Posts]
            respondWithMediaType(MediaTypes.`application/json`) {
              onComplete(createPost(access_token, post)) {
                case Success(value) => {
                  complete(StatusCodes.Created, Map("postid" -> value))
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

  private val route_query = {
    path("posts") {
      get {
        parameters('access_token) { (access_token) =>
          respondWithMediaType(MediaTypes.`application/json`) {
            onComplete(queryPosts(access_token)) {
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
