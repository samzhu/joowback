package com.joow.hazelcast

import com.hazelcast.core.IMap
import com.joow.entity.AccessToken

/**
 * Created by SAM on 2015/3/30.
 */
trait TokenHz extends HzHelper {

  private val mapName = "token"

  private def getTokenMap(): IMap[String, AccessToken] = {
    getInstance().getMap[String, AccessToken](mapName)
  }

  protected def saveAccessTokenHz(accessToken: AccessToken): Unit = {
    getTokenMap().put(accessToken.accessToken, accessToken)
  }

  /**
   * 取得Token
   * @param tokenStr
   * @return
   */
  protected def getAccessTokenHz(tokenStr: String): AccessToken = {
    val accessToken: AccessToken = getTokenMap().get(tokenStr)
    accessToken
  }


}
