package com.joow.app

import akka.actor.ActorSystem
import com.joow.hazelcast.HzHelper

import com.joow.route.{AuthRouting, BlogRouting, AccountRouting}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.SimpleRoutingApp
import spray.http.MediaTypes

object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

/**
 * Created by SAM on 2015/2/27.
 */
object BackBoot extends App with SimpleRoutingApp with HzHelper{
  implicit val actorSystem = ActorSystem()
  //init
  //HzHelper.initInstance()
  initInstance()

  startServer(interface = "localhost", port = 8080) {
    path("user") {
      post {
        entity(as[String]) { requestbody =>
          println("requestbody => " + requestbody)
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              requestbody
            }
          }
        }
      }
    } ~
      pathPrefix("api") {
        AccountRouting.route ~
          BlogRouting.route_create ~
          AuthRouting.route_create
      } ~
      path("") {
        compressResponse() {
          getFromResource("web/index.html")
        }
      } ~
      pathPrefix("web") {
        compressResponse() {
          getFromResourceDirectory("web")
        }
      }
  }
}
