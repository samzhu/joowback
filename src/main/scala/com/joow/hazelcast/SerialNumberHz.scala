package com.joow.hazelcast

import com.hazelcast.core.IMap

/**
 * Created by samzhu on 2015/4/1.
 */
trait SerialNumberHz extends HzHelper {
  private val mapName = "serialnumber"

  private def getSerialNumberMap(): IMap[String, BigInt] = {
    getInstance().getMap[String, BigInt](mapName)
  }

  protected def getNextValue(mapkey: String): BigInt = {
    val map: IMap[String, BigInt] = getSerialNumberMap()
    var value: BigInt = map.get(mapkey)
    if (value == null) {
      value = BigInt(1)
      map.put(mapkey, value)
    }else{
      value = value + 1
      map.replace(mapkey, value)
    }
    value
  }
}
