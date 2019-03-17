package cluster

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberEvent, MemberUp, UnreachableMember}
import cluster.GWProtocol.{mapWorkers, _}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable._
import scala.util.Random


class Orchestrator extends Actor {
  var workers = IndexedSeq.empty[ActorRef]
  var mappedWorkers = Map[ActorRef, List[Int]]()
  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[MemberUp], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case mapWorkers(map) =>
      mappedWorkers = map
    case getNode(id, client) if workers.isEmpty =>
      println("Service unavailable, cluster doesn't have any worker node.")
    case getNode(id, client) =>
      var workerRef: ActorRef = findWorker(id)
      if (workerRef != null) {
        workerRef forward getNode(id, client)
      } else {
        println(s"Orchestrator ${self.path} : Node doesn't exist in our system.")
      }
    case deleteNodeOp: deleteNode =>
      workers(Random.nextInt(workers.size)) forward deleteNodeOp
    case registerWorker if !(workers.contains(sender())) =>
      workers = workers :+ sender
      context watch sender
    case response(node) =>
      print("\n------------------------------------------------------\n")
      println(s"Client ${self.path} received response Node : ${node}")
      print("\n------------------------------------------------------\n")
    case error(message) =>
      print("\n------------------------------------------------------\n")
      println(s"Client ${self.path} received error response : ${message}")
      print("\n------------------------------------------------------\n")
    case Terminated(a) =>
       workers = workers.filterNot(_ == a)
   case _ =>
      println("invalid response ! " + _)
  }

  def findWorker(id: Int): ActorRef = {
    mappedWorkers foreach {
      case (ref, list) => {
        if (id >= list(0) && id <= list(1)) return ref
      }
    }
    return null
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