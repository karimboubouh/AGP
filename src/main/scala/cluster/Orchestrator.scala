package cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import cluster.GWProtocol.{mapWorkers, _}
import com.typesafe.config.ConfigFactory
import graph.Node

import collection.mutable._
import scala.util.Random


class Orchestrator extends Actor {
  var workers = IndexedSeq.empty[ActorRef]
  var mappedWorkers = Map[ActorRef, List[Int]]()
  def receive = {
    case mapWorkers(map) =>
      mappedWorkers = map
    case getNode(id, client) if workers.isEmpty =>
      println("Service unavailable, cluster doesn't have any worker node.")
    case getNode(id, client) =>
      var workerRef : ActorRef = null
      mappedWorkers foreach {
        case (ref, list) => {
          if(id >= list(0) && id <= list(1)) workerRef = ref
        }
      }
      if (workerRef != null){
        workerRef forward getNode(id, client)
      }else{
        println(s"Orchestrator ${self.path} : Node doesn't exist in our system.")
      }
    case deleteNodeOp: deleteNode =>
      workers(Random.nextInt(workers.size)) forward deleteNodeOp
    case registerWorker if !(workers.contains(sender())) =>
      workers = workers :+ sender()
      context watch (sender())
    case response(node : graph.Node) =>
      println(s"Client ${self.path} received response Node : ${node}")
    case error(message) =>
      println(s"Client ${self.path} received error response : ${message}")
    case Terminated(a) =>
      workers = workers.filterNot(_ == a)
  }
}

object Orchestrator {
  private var _Orchestrator: ActorRef = _
  def initiate() = {
    val config = ConfigFactory.load().getConfig("Orchestrator")
    val system = ActorSystem("ClusterSystem", config)
    _Orchestrator = system.actorOf(Props[Orchestrator], name = "Orchestrator")
  }
  def getOrchestrator = _Orchestrator
}