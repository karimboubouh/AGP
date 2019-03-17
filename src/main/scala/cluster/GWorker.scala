package cluster

import java.io._

import akka.actor.{Actor, ActorRef, ActorSystem, Props, RootActorPath}
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster._
import cluster.GWProtocol._
import com.typesafe.config.ConfigFactory
import graph.Node
import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.mutable.Graph

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GWorker(subGraph: Graph[Node, UnDiEdge]) extends Actor with Serializable {

  // Actor state
  var g = subGraph

  // Cluster initiate
  val cluster = Cluster(context.system)

  // ---------------------- Actor Events --------------------------------------
  // subscribe to cluster changes, MemberUp & re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp])
    context.system.scheduler.schedule(0 seconds, 20 seconds) {
      persistWorkerState(self.path.name, g)
    }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    g = restoreWorkerState(self.path.name)
    println("State restored from disk!")
  }

  override def postStop(): Unit = {
    println("i'm about to stop....")
    cluster.unsubscribe(self)
  }

  // ------------------------- Message handling -------------------------------
  def receive = {
    case getNode(id, client) =>
      val node = findNode(id)
      if (node != null) {
        // TODO first message doesn't get received by the clients !
        client ! ""
        client ! response(node)
      } else {
        client ! error(s"Node doesn't exist in actor ${self.path}")
      }
    case deleteNode(id) =>
      println(s"I'm a worker with path: ${self} and I received deleteNode ${id} operation.")
    case MemberUp(member) =>
      if (member.hasRole("Orchestrator")) {
        context.actorSelection(RootActorPath(member.address) / "user" / "Orchestrator") !
          registerWorker
      }
    case "Exception" =>
      println("Received Exception ! from " + sender.path)
      throw new Exception("Boom!")
  }

  // ------------------------- Local Actor Functions --------------------------
  def findNode(id: Int): Node = {
    for (x <- g.nodes.toIterator) {
      if (x.value.nodeId == id)
        return x.value
    }
    return null
  }

  def persistWorkerState(file: String, state: Graph[Node, UnDiEdge]): Unit = {
    val oos = new ObjectOutputStream(new FileOutputStream("/tmp/" + file))
    oos.reset()
    oos.writeObject(state)
    oos.reset()
    oos.close
  }

  def restoreWorkerState(file: String): Graph[Node, UnDiEdge] = {
    val ois = new ObjectInputStream(new FileInputStream("/tmp/" + file))
    val restoredState = ois.readObject.asInstanceOf[Graph[Node, UnDiEdge]]
    ois.close
    return restoredState
  }

}

object GWorker {
  def initiate(port: Int, subGraph: Graph[Node, UnDiEdge]): ActorRef = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load().getConfig("GWorker"))
    val system = ActorSystem("ClusterSystem", config)
    return system.actorOf(Props(classOf[GWorker], subGraph), s"GWorker-${port}")
  }
}
