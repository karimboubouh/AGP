package cluster

import cluster.GWProtocol._

object ClusterApp extends App {

//  generateFrontendActors(4)
//  generateBackendGraphActors()

  //initiate frontend node
  Orchestrator.initiate()

  //initiate three nodes from backend
  GWorker.initiate(2552)
  GWorker.initiate(2560)
  GWorker.initiate(2561)

  Thread.sleep(10000)

  Orchestrator.getOrchestrator ! getNode(4)

}