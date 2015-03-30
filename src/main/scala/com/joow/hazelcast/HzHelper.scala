package com.joow.hazelcast

import com.hazelcast.core.HazelcastInstance


/**
 * Created by SAM on 2015/3/29.
 */
object HzHelper{
  var hz:HazelcastInstance = null
}

trait HzHelper{
  import com.hazelcast.config.{Config,MapIndexConfig, InMemoryFormat, MapConfig}
  import com.hazelcast.core.{Hazelcast, IMap}

  /**
   *
   */
  def initInstance(): Unit ={
    val config:Config = new Config()
    HzHelper.hz = Hazelcast.newHazelcastInstance(config)

    //
    val accountConfig:MapConfig = config.getMapConfig("account");
    accountConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
    //
    accountConfig.addMapIndexConfig(new MapIndexConfig("email", true));

  }

  def getInstance(): HazelcastInstance ={
    HzHelper.hz
  }

  def getMap(mapName:String): IMap[String,Object] ={
    HzHelper.hz.getMap[String,Object](mapName)
  }

}
