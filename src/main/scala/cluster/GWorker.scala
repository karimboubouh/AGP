package cluster

import akka.actor.{Actor, ActorSystem, Props, RootActorPath}
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster._
import cluster.GWProtocol._
import com.typesafe.config.ConfigFactory

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
  def initiate(port: Int) = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load().getConfig("GWorker"))
    val system = ActorSystem("ClusterSystem", config)
    val GWorker = system.actorOf(Props[GWorker], name = "GWorker")
  }
}
