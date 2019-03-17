package cluster

import akka.actor.ActorRef
import cluster.GWProtocol._
import graph.Client

import scala.collection.mutable.{ListBuffer, Map}
import scala.util.Random

object DGraphApp extends App {

  // create a map of workers and its nodes
  var mappedWorkers = Map[ActorRef, List[Int]]()

  //initiate Orchestrator node
  Orchestrator.initiate()

  // generate graph and start a worker per subGraph
  var workers = generateGraphs(100)

  // send map to orchestrator
  val or = Orchestrator.getOrchestrator
  or ! mapWorkers(mappedWorkers)

  // wait for actors to initialize
  Thread.sleep(2000)

  // send requests and Exceptions
  for (i <- 1 to 100){
    val x = Random.nextInt(workers.size)
    val id = Random.nextInt(100)
    workers(x) ! "Exception"
    workers(x) ! getNode(id, or)
  }

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