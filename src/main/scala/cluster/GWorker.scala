package cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props, RootActorPath}
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster._
import cluster.GWProtocol._
import com.typesafe.config.ConfigFactory
import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.mutable.Graph

class GWorker extends Actor {

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, MemberUp
  // re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive = {
    case getNode(id) =>
      println(s"I'm a worker with path: ${self} and I received getNode ${id} operation.")
    case deleteNode(id) =>
      println(s"I'm a worker with path: ${self} and I received deleteNode ${id} operation.")
    case MemberUp(member) =>
      if (member.hasRole("Orchestrator")) {
        context.actorSelection(RootActorPath(member.address) / "user" / "Orchestrator") !
          registerWorker
      }
  }
}

object GWorker {
  def initiate(port: Int, subGraph : Graph[Any, UnDiEdge]): ActorRef = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load().getConfig("GWorker"))
    val system = ActorSystem("ClusterSystem", config)
    return system.actorOf(Props(classOf[GWorker], subGraph), s"GWorker-${port}")
  }
}
