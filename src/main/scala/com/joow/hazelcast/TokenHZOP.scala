package com.joow.hazelcast

/**
 * Created by samzhu on 2015/3/29.
 */
object TokenHZOP extends HzHelper{
  import com.hazelcast.core.IMap
  import com.joow.entity.AccessToken
  val mapName = "token"

  def saveAccessToken(accessToken:AccessToken): Unit ={
    val tokenMap = getMap(mapName)
    tokenMap.put(accessToken.accessToken,accessToken)
  }

  def getAccessToken(tokenStr:String): AccessToken ={
    val tokenMap:IMap[String, AccessToken] = getInstance().getMap[String,AccessToken](mapName)
    val accessToken:AccessToken = tokenMap.get(tokenStr)
    accessToken
  }
}
