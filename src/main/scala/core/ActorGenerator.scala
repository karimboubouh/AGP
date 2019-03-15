package core

import akka.actor.{Actor, Props}
import core.AGProtocol.generateActor


object AGProtocol {
  case class generateActor(name:String,graph:List[Any])
}
class ActorGenerator extends Actor{
  override def receive: Receive = {
    case generateActor(name,graph)=>
      val ag=context.actorOf(Props(classOf[GraphActor],name,graph),name+"Actor")
      sender ! ag
    case _=>
      println("unknown operation")
  }
}
