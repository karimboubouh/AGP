package cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import cluster.GWProtocol._
import com.typesafe.config.ConfigFactory

import scala.util.Random

class Orchestrator extends Actor {
  var workers = IndexedSeq.empty[ActorRef]

  def receive = {
    case getNode(id) if workers.isEmpty =>
      println("Service unavailable, cluster doesn't have any worker node.")
    case getNodeOp: getNode =>
      println("Orchestrator: I'll forward getNode operation to worker node to handle it.")
      workers(Random.nextInt(workers.size)) forward getNodeOp
    case deleteNodeOp: deleteNode =>
      println("Orchestrator: I'll forward deleteNode operation to worker node to handle it.")
      workers(Random.nextInt(workers.size)) forward deleteNodeOp
    case registerWorker if !(workers.contains(sender())) =>
      workers = workers :+ sender()
      context watch (sender())
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