package cluster

import akka.actor.ActorRef
import graph.Node

import scala.collection.mutable.Map

object GWProtocol {

  case class getNode(id: Int, client : ActorRef)
  case class deleteNode(id: Int)
  case class mapWorkers(map: Map[ActorRef, List[Int]])
  case class response(r : graph.Node)
  case class error(r : String)
  case object registerWorker

}
