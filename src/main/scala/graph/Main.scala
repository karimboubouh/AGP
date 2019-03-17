package graph

import cluster.DGraphApp.mappedWorkers
import cluster.GWorker

object Main extends App {
  val person = new Client()
//  val x = person.generateGraph(1, 11)

  var initNode = 1
  for( i <- 1 to 5){
    val (subGraph, nodeId) = person.generateGraph(initNode, i)
    initNode = nodeId
  }
//  for (c1 : Any <- nodes : Any) yield println(c1)
  //println(s"Last nodeId : ${x._2} | GraphSet(${x._1.nodes.size})")



}
