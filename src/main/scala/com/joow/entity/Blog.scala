package com.joow.entity

import org.elasticsearch.search.SearchHit

/**
 * Bolg Entity
 * Created by SAM on 2015/3/21.
 */
case class Blog (title:String, content:String, tags:List[String])
case class Tag (tag:String)

object Blog {
  def apply(sh: SearchHit): Option[Blog] = try {
    val map = sh.sourceAsMap
    Some(Blog(
      title = map.get("title").asInstanceOf[String],
      content = map.get("content").asInstanceOf[String],
      tags = map.get("tags").asInstanceOf[List[String]]
    ))
  } catch {
    case ex: Exception =>{
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}

object Tag {
  def apply(sh: SearchHit): Option[Tag] = try {
    val map = sh.sourceAsMap
    Some(Tag(
      tag = map.get("tag").asInstanceOf[String]
    ))
  } catch {
    case ex: Exception =>{
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}