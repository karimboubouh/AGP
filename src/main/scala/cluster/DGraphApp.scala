package cluster

import akka.actor.ActorRef
import cluster.GWProtocol._
import graph.{Client, Graph22}

import scala.collection.mutable.ListBuffer

object DGraphApp extends App {

  //initiate Orchestrator node
  Orchestrator.initiate()

  // generate graph and start a worker per subGraph
  var workers = generateGraphs(10)

  // wait for actors to initialize
  Thread.sleep(10000)

  // send requests
  Orchestrator.getOrchestrator ! getNode(4)

  // ------------------------- Functions -------------------------
  def generateGraphs(workersNumber : Int) : ListBuffer[ActorRef] = {
    // generate $workersNumber random graphs and workers
    var workers = ListBuffer[ActorRef]()
    val c = new Client()
    var i = 0
    var initPort = 2600
    var initNode = 1
//    var subGraph = null
    for( i <- 1 to workersNumber){
      val (subGraph, nodeId) = c.generateGraph(initNode, i)
      initNode = nodeId
      workers += GWorker.initiate(initPort, subGraph)
      initPort += 1
    }

     return workers
  }
}