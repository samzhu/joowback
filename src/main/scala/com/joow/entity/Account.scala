package com.joow.entity

import java.util

import org.elasticsearch.search.SearchHit

/**
 * Created by SAM on 2015/2/27.
 */

case class Account(email: String, passwd: String, nickname: String, createDate: String)

object Account {
   def apply(sh: SearchHit): Option[Account] = try {
    val map = sh.sourceAsMap
    Some(Account(
      //id    = m("_id")  .asInstanceOf[String],
      email = map.get("email").asInstanceOf[String],
      "",
      nickname = map.get("nickname").asInstanceOf[String],
      createDate = map.get("createDate").asInstanceOf[String]
      //roles = m("roles").asInstanceOf[java.util.List[String]],
      //note  = if (noteES == "") None else Some(noteES))
    ))
  } catch {
    case ex: Exception =>{
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}


