package com.joow.entity

/**
 * 個人資料 用戶代碼、匿名、大頭照連結、追蹤誰、被誰追蹤
 * Created by SAM on 2015/4/5.
 */
case class Profile(userid: String, nickname: String, photo: Link, following: Seq[Link], followers: Seq[Link], timecreate: String)

object Profile {

  import java.util.Map
  import scala.collection.JavaConversions._

  def apply(map: Map[String, AnyRef]): Option[Profile] = try {
    Some(Profile(
      userid = map.get("userid").asInstanceOf[String],
      nickname = map.get("nickname").asInstanceOf[String],
      photo = map.get("photo").asInstanceOf[Link],
      following = asScalaBuffer(map.get("follow").asInstanceOf[java.util.ArrayList[Link]]),
      followers = asScalaBuffer(map.get("followers").asInstanceOf[java.util.ArrayList[Link]]),
      timecreate = map.get("timecreate").asInstanceOf[String])
    )
  } catch {
    case ex: Exception => {
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}
