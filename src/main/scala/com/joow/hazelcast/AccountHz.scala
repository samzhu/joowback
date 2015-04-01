package com.joow.hazelcast

import com.hazelcast.core.IMap
import com.joow.entity.Account

/**
 * Created by SAM on 2015/3/31.
 */
trait AccountHz extends HzHelper with SerialNumberHz {
  private val mapName = "account"


  private def getAccountMap(): IMap[String, Account] = {
    getInstance().getMap[String, Account](mapName)
  }


  def createAccountToHz(account: Account): Account = {
    val rs = getAccountMap().putIfAbsent(account.email, account)
    var saveaccount:Account = null
    if (rs == null) {
      //when save success then set userid
      saveaccount = account.copy(userid = Option(getNextValue("userid").toString()))
      getAccountMap().replace(saveaccount.email, saveaccount)
    } else {
      throw new Exception("帳號已存在")
    }
    saveaccount
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
