package com.joow.route

import com.joow.app.Json4sProtocol
import com.joow.service.PhotosOperations
import org.apache.commons.codec.binary.Base64
import spray.http._
import spray.routing.SimpleRoutingApp
import scala.util.{Failure, Success}

/** 儲存照片
  * Created by SAM on 2015/4/3.
  */
object PhotosRouting extends SimpleRoutingApp with PhotosOperations {

  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val route = {
    route_create ~ route_get
  }

  private val route_create = {
    path("photos") {
      post {
        parameters('access_token) { (access_token) =>
          entity(as[MultipartFormData]) { formdata =>
            onComplete(savePhoto(access_token, formdata.get("photo").get.entity.data.toByteArray, Option(null))) {
              case Success(value) => {
                complete {
                  import Json4sProtocol._
                  StatusCodes.Created -> Map("photoid" -> value)
                }
              }
              case Failure(ex) => {
                complete {
                  import Json4sProtocol._
                  StatusCodes.InternalServerError -> Map("msg" -> ex.getMessage)
                }
              }
            }
            //formdata.fields.foreach( bodypart =>
            //  savePhoto(access_token,bodypart.entity.data.toByteArray,Option(null))
            //)
            /*
            formdata.get("uploadField") match {
                case Some(imageEntity) =>
                  val size = imageEntity.entity.data.length
                  println(s"Uploaded $size")
                  Map("postid" -> size)
                case None =>
                  println("No files")
                  "Not OK"
              }
             */
          }
        }
      }
    }
  }

  private val route_get = {
    path("photos" / Segment) { photoid: String =>
      get {
        parameters('access_token) { access_token =>
          respondWithMediaType(MediaTypes.`image/png`) {
            onComplete(getPhoto(access_token, photoid)) {
              case Success(value) => {
                complete(StatusCodes.OK, value)
              }
              case Failure(ex) => {
                complete {
                  import Json4sProtocol._
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
