package com.joow.entity

import org.elasticsearch.search.SearchHit

/**
 * Created by SAM on 2015/2/27.
 */
case class Account(userid: Option[String], email: String, passwd: String, nickname: String, createDate: Option[String])

object Account {
  def apply(sh: SearchHit): Option[Account] = try {
    val map = sh.sourceAsMap
    Some(Account(
      userid = Option(map.get("userid").asInstanceOf[String]),
      email = map.get("email").asInstanceOf[String],
      "",
      nickname = map.get("nickname").asInstanceOf[String],
      createDate = Option(map.get("createDate").asInstanceOf[String])
      //roles = m("roles").asInstanceOf[java.util.List[String]],
      //note  = if (noteES == "") None else Some(noteES))
    ))
  } catch {
    case ex: Exception => {
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}


