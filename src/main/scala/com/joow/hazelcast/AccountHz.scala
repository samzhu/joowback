package com.joow.hazelcast

import com.hazelcast.core.IMap
import com.joow.entity.Account

/**
 * Created by SAM on 2015/3/31.
 */
trait AccountHz extends HzHelper{
  private val mapName = "account"

  private def getAccountMap(): IMap[String, Account] = {
    getInstance().getMap[String, Account](mapName)
  }

  def createAccountToHz(account: Account): Unit = {
    val rs = getAccountMap().putIfAbsent(account.email, account)
    if (rs != null) {
      throw new Exception("帳號已存在")
    }
  }

  def modifyAccountToHz(account: Account): Unit = {
    val accountOrg: Account = getAccountMap().get(account.email).copy(nickname = account.nickname)
    getAccountMap().replace(account.email, accountOrg)
  }

  protected def getAccountByEmail(email: String): Account = {
    val account: Account = getAccountMap().get(email)
    account
  }
}
