package com.joow.hazelcast

import com.hazelcast.core.IMap
import com.joow.entity.{AccessToken, Account}
import com.joow.hazelcast.TokenHZOP._

/**
 * Created by SAM on 2015/3/30.
 */
trait AccountHz extends HzHelper {
  val mapName = "account"

  def createAccountToHz(account: Account): Unit = {
    val accMap: IMap[String, Account] = getInstance().getMap[String, Account](mapName)
    val rs = accMap.putIfAbsent(account.email, account)
    if (rs != null) {
      throw new Exception("帳號已存在")
    }
  }
}
