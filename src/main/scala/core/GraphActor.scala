package core

import akka.actor.Actor
import core.AProtocol.generateConfFile
object AProtocol{
  case class generateConfFile(host:String,ip:String,port:Int)
}
class GraphActor(name:String,graph: List[Any],host:String,ip:String,port:Int) extends Actor{
  override def receive: Receive = {
    case generateConfFile(host,ip,port)=>
      //TODO generate configuration File
  }

}
