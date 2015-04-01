package com.joow.entity

import akka.http.model.japi.JavaMapping.Implicits.AddAsScala
import org.elasticsearch.search.SearchHit

import scala.collection.mutable.Buffer

/**
 * Bolg Entity
 * Created by SAM on 2015/3/21.
 */
case class Posts(title: String, content: String, tags: Seq[String], ownerid: Option[String])

object Posts {
  import scala.collection.JavaConversions._
  def apply(sh: SearchHit): Option[Posts] = try {
    val map = sh.sourceAsMap
    Some(Posts(
      title = map.get("title").asInstanceOf[String],
      content = map.get("content").asInstanceOf[String],
      tags = asScalaBuffer(map.get("tags").asInstanceOf[java.util.ArrayList[String]]),
      //tags = map.get("tags").asInstanceOf[java.util.ArrayList[String]].asScala,
      ownerid = Option(map.get("ownerid").asInstanceOf[String])
    ))
  } catch {
    case ex: Exception => {
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }
}
