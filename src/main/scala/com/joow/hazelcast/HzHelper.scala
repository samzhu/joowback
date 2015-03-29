package com.joow.hazelcast

import com.hazelcast.config.{MapIndexConfig, InMemoryFormat, MapConfig}
import com.hazelcast.core.IMap

import scala.collection.JavaConverters._

/**
 * Created by SAM on 2015/3/29.
 */
trait HzHelper extends App{
  import com.hazelcast.config.Config
  import com.hazelcast.core.{Hazelcast, HazelcastInstance}
  var hz:HazelcastInstance = null

  initInstance()

  val token = hz.getMap[String,Object]("token")

  token.put("zsdd","我是token")
  println(token.get("zsdd"))




  /**
   *
   */
  def initInstance(): Unit ={
    val config:Config = new Config()
    hz = Hazelcast.newHazelcastInstance(config)

    //
    val accountConfig:MapConfig = config.getMapConfig("account");
    accountConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
    //
    accountConfig.addMapIndexConfig(new MapIndexConfig("email", true));

  }

  def getInstance(): HazelcastInstance ={
    hz
  }

  def getMap(mapName:String): IMap[String,Object] ={
    hz.getMap[String,Object](mapName)
  }

}
