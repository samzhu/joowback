package com.joow.utils

import java.io.StringWriter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * Created by SAM on 2015/3/8.
 */
object Print {

  /**
   * 印出成JSON格式
   * @param obj
   */
  def toJson( obj:Any ) : Unit = {
    val mapper = new ObjectMapper
    val out = new StringWriter
    mapper.writeValue(out, obj)
    println(out.toString)
  }
}
