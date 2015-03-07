package com.joow.app

import akka.actor.ActorSystem
import com.joow.route.AccountRouting
import spray.routing.SimpleRoutingApp
import spray.http.MediaTypes

/**
 * Created by SAM on 2015/2/27.
 */
object BackBoot extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()

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
      AccountRouting.route

  }

}