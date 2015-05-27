package com.joow.entity

import java.util.Map

import akka.http.model.japi.JavaMapping.Implicits.AddAsScala
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.{SearchHits, SearchHit}

import scala.Predef
import scala.collection.mutable.{ListBuffer, Buffer}
import scala.concurrent.Future

/**
 * Bolg Entity
 * Created by SAM on 2015/3/21.
 */
case class Posts(title: String, content: String, tags: Seq[String], location: Option[Location], ownerid: Option[String])

object Posts {

  import scala.collection.JavaConversions._

  def apply(map: Map[String, AnyRef]): Posts = {
    Posts(
      title = map.get("title").asInstanceOf[String],
      content = map.get("content").asInstanceOf[String],
      tags = asScalaBuffer(map.get("tags").asInstanceOf[java.util.ArrayList[String]]),
      location = Location(map.get("location").asInstanceOf[java.util.HashMap[String, Double]]),
      ownerid = Option(map.get("ownerid").asInstanceOf[String])
    )
  }

  def fromSearchHit(sh: SearchHit): Option[Posts] = try {
    val map = sh.sourceAsMap
    Some(Posts(map))
  } catch {
    case ex: Exception => {
      ex.printStackTrace()
      println("Missing file exception" + ex)
      None
    }
  }


  def fromSearchHit(sh: SearchHits): Predef.Map[String, Any] = {
    println("fromSearchHit = " + sh )
    var list: Predef.Map[String, Any] = null;
    try {
      val hits: ListBuffer[Any] = new ListBuffer()
      sh.getHits().foreach(u =>
        hits += Predef.Map("_id" -> u.getId, "_score" -> u.getScore, "post" -> Posts.fromSearchHit(u))
      )
      var maxScore:Float = 0
      if(!sh.getMaxScore.equals(Float.NaN))
        maxScore = sh.getMaxScore
      list = Predef.Map("total" -> sh.getTotalHits, "max_score" -> maxScore, "hits" -> hits)
    } catch {
      case ex: Exception => {
        ex.printStackTrace()
        println("Missing file exception" + ex)
        None
      }
    }
    list
  }

}