package core

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {


  def remoteConfig(hostname: String, port: Int, commonConfig: Config): Config = {
    val configStr = s"""
                       |akka.remote.netty.hostname = $hostname
                       |akka.remote.netty.port = $port
  """.stripMargin

    ConfigFactory.parseString(configStr).withFallback(commonConfig)
  }

  //example of configuring a remote  actor
  val appConfig = ConfigFactory.load
  val sys1 = ActorSystem("sys1", remoteConfig(args(0), args(1).toInt, appConfig))




}
