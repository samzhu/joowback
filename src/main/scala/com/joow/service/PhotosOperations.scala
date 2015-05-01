package com.joow.service

import com.joow.elastic.PhotosEs
import com.joow.entity.{Location, Photo, Account}
import com.joow.utils.UtilTime
import net.sf.jmimemagic.Magic
import org.apache.commons.codec.binary.Base64
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexResponse
import scala.concurrent.{Promise, Future}
import scala.util.{Failure, Success, Random}

/**
 * Created by SAM on 2015/4/3.
 */
trait PhotosOperations extends PhotosEs with AuthOperations {

  import scala.concurrent.ExecutionContext.Implicits.global

  def savePhoto(access_token: String, photo_rawdata: Array[Byte], location: Option[Location]): Future[String] = {
    val account: Account = getAccountByToken(access_token)
    val photoid: String = "1" + "_" + Random.nextLong()
    val createtime: String = UtilTime.getUTCTime()
    val mimetype: String = Magic.getMagicMatch(photo_rawdata).getMimeType()
    val photo: Photo = Photo(photoid, Base64.encodeBase64String(photo_rawdata), mimetype, location, createtime, account.userid)
    val resp: Future[IndexResponse] = savePhotoEs(photo)
    val promise = Promise[String]()
    Future {
      resp onComplete {
        case Success(result) => {
          promise.success(result.getId)
        }
        case Failure(failure) => {
          promise.failure(failure)
        }
      }
    }
    promise.future
  }

  def getPhoto(access_token: String, photoid: String): Future[Array[Byte]] = {
    val resp:Future[GetResponse] = getPhotoEs(photoid)
    val promise = Promise[Array[Byte]]()
    Future {
      resp onComplete {
        case Success(result) => {
          val photo_rawdata: Array[Byte] = Base64.decodeBase64(result.getSourceAsMap.get("rowdata").asInstanceOf[String])
          promise.success(photo_rawdata)
        }
        case Failure(failure) => {
          promise.failure(failure)
        }
      }
    }
    promise.future


  }


  /*
  def saveFile(bodypart:BodyPart): Unit ={
    import java.nio.file.Paths
    import java.nio.file.Path
    import java.nio.file.Files
    val p: Path = Paths.get("D:/test.png")
    Files.write(p, bodypart.entity.data.toByteArray)

    val by = Files.readAllBytes(Paths.get("D:/gcm-a-modr.png"))
  }
  */
}
