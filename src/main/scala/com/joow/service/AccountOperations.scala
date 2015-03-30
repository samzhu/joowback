package com.joow.service

import com.joow.entity.Account
import com.joow.hazelcast.AccountHz

/**
 * Created by SAM on 2015/3/30.
 */
trait AccountOperations extends AccountHz {

  def createAccount(account: Account): Unit = {
    createAccountToHz(account)
  }

  def modifyAccount(account: Account): Unit ={
    modifyAccountToHz(account)
  }

}
