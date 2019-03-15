package core

import akka.actor.{Actor, Props}
object AProtocol{

}
class GraphActor(name:String,graph: List[Any]) extends Actor{
  override def receive: Receive = {
    case _=>
      println("unknown operation")
  }

}
