package com.joow.utils

import java.text.SimpleDateFormat
import java.util.SimpleTimeZone

/**
 * Created by SAM on 2015/3/31.
 */
object UtilTime {
  val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'")
  sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"))

  /**
   * 取得中原時間
   * @return
   */
  def getUTCTime(): String ={
    sdf.format(System.currentTimeMillis)
  }
}
