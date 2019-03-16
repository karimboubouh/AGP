package cluster

import java.io._
import java.util.{Timer, TimerTask}
import scala.concurrent.duration._
import akka.actor.{Actor, ActorRef, ActorSystem, Props, RootActorPath}
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster._
import cluster.GWProtocol._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import graph.Node
import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.mutable.Graph

class GWorker(subGraph: Graph[Node, UnDiEdge]) extends Actor with Serializable {

  // Actor state
  var g = subGraph

  // Cluster initiate
  val cluster = Cluster(context.system)

  // subscribe to cluster changes, MemberUp & re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp])
    context.system.scheduler.schedule(10 seconds, 20 seconds) {
      println("************************************||__--> preStart")
      persistWorkerState()
    }
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive = {
    case getNode(id, client) =>
      println("-------------------------------------> " + id + " ----------- " + client)
      val node = findNode(id)
      if (node != null) {
        println("------------------------> " + node.getClass)
        client ! response(node)
      } else {
        println("------------------------> " + node)
        client ! error(s"Node doesn't exist in actor ${self.path}")
      }
    case deleteNode(id) =>
      println(s"I'm a worker with path: ${self} and I received deleteNode ${id} operation.")
    case MemberUp(member) =>
      if (member.hasRole("Orchestrator")) {
        context.actorSelection(RootActorPath(member.address) / "user" / "Orchestrator") !
          registerWorker
      }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    val t = new Timer()
    val task = new TimerTask {
      def run() = persistWorkerState()
    }
    t.schedule(task, 10000L, 10000L)
    //task.cancel()
  }

  def findNode(id: Int): Node = {
    for (x <- g.nodes.toIterator) {
      if (x.value.nodeId == id)
        return x.value
    }
    return null
  }

  def persistWorkerState(): Unit = {
    // (1) create a Stock instance
    val nflx = new GWorker(subGraph)
    // (2) write the instance out to a file
    val oos = new ObjectOutputStream(new FileOutputStream("/tmp/GWorker"))
    oos.writeObject(nflx)
    oos.close
    // (3) read the object back in
    val ois = new ObjectInputStream(new FileInputStream("/tmp/GWorker"))
    val stock = ois.readObject.asInstanceOf[Actor]
    ois.close
    // (4) print the object that was read back in
    println("worker has been stored")
    println(stock)
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
