package com.joow.route

import com.joow.app.Json4sProtocol
import com.joow.entity.{Blog, RsHeader, Response}
import com.joow.service.BlogOperations
import org.json4s.{DefaultFormats, Formats}
import org.json4s.JsonAST.JObject
import spray.http.MediaTypes
import spray.httpx.Json4sSupport
import spray.routing.SimpleRoutingApp

/**
 * Created by SAM on 2015/3/21.
 */

object BlogRouting extends SimpleRoutingApp with BlogOperations{
  import Json4sProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_create
  }

  /*
  POST graph.facebook.com
  /{user-id}/feed?
    message={message}&
    access_token={access-token}
   */
  private val route_create = {
    path("blog") {
      post {
        entity(as[JObject]) { jsonObj =>
          val blog = jsonObj.extract[Blog]
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              createBlog("",blog)

              val body: Map[Any, Any] = Map("_id" -> blog.title)
              val res = Response(RsHeader("0"), body)
              res

            }
          }
        }
      }
    }
  }
}
