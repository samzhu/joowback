package com.joow.entity

/**
 * Created by SAM on 2015/5/1.
 */
case class Location(lat: Double, lon: Double)

//java.util.HashMap
object Location {

  import scala.collection.JavaConversions._

  def apply(map: java.util.HashMap[String, Double]): Option[Location] = {
    var location = None
    if (map != null) {
      try {
        Some(Location(
          lat = map.get("lat").asInstanceOf[Double],
          lon = map.get("lon").asInstanceOf[Double])
        )
      } catch {
        case ex: Exception => {
          ex.printStackTrace()
          println("Missing file exception" + ex)
          location = None
        }
      }
    }
    location
  }
}