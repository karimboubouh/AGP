package core

import akka.actor.Actor
import java.io._
class ConfigurationFileGenerator(host:String,ip:String,port:Int) extends Actor{

  override def receive: Receive = {
    case _ =>
      println("dddd")
  }

}
