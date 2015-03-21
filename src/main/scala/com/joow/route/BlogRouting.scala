package com.joow.route

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


  val route_create = {
    path("blog") {
      post {
        entity(as[JObject]) { jsonObj =>
          val blog = jsonObj.extract[Blog]
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              create(blog)

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
