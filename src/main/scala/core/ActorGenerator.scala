package core

import akka.actor.Actor
import core.AGProtocol.generateActor


object AGProtocol {
  case class generateActor(name:String,graph:List[Any],host:String,ip:String,port:Int)
}
class ActorGenerator extends Actor{
  override def receive: Receive = {
    case generateActor(name,praph,host,ip,port)=>
      //TODO generate actor & file confugaration
  }
}
