package cluster

import akka.actor.ActorRef
import cluster.GWProtocol._
import graph.Client

import scala.collection.mutable.{ListBuffer, Map}

object DGraphApp extends App {

  // create a map of workers and its nodes
  var mappedWorkers = Map[ActorRef, List[Int]]()

  //initiate Orchestrator node
  Orchestrator.initiate()

  // generate graph and start a worker per subGraph
  var workers = generateGraphs(2)

  // send map to orchestrator
  Orchestrator.getOrchestrator ! mapWorkers(mappedWorkers)

  // wait for actors to initialize
  Thread.sleep(5000)

  // send requests
  Orchestrator.getOrchestrator ! getNode(4, Orchestrator.getOrchestrator)

  // ------------------------- Functions -------------------------
  def generateGraphs(workersNumber: Int): ListBuffer[ActorRef] = {
    // generate $workersNumber random graphs and workers
    var workers = ListBuffer[ActorRef]()
    val c = new Client()
    var i = 0
    var initPort = 2600
    var initNode = 1
    //    var subGraph = null
    for (i <- 1 to workersNumber) {
      val (subGraph, nodeId) = c.generateGraph(initNode, i)
      var w = GWorker.initiate(initPort, subGraph)
      mappedWorkers += (w -> List(initNode, nodeId - 1))
      initNode = nodeId
      workers += w
      initPort += 1
    }
    return workers
  }
}