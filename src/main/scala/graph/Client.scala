package graph

import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.generator.{NodeDegreeRange, RandomGraph}
import scalax.collection.mutable.Graph
import scalax.collection.edge.LDiEdge

case class Node(nodeId: Int, name: String, balance: Int, subGraphId: Int)

class Client{

  var counter = 0

  object ClientData {
    val firstNames = Set("Alen", "Alice", "Bob", "Jack", "Jim", "Joe", "Kate", "Leo", "Tim", "Tom").to[Vector]
    val firstNamesSize = firstNames.size

    val surnames = Set("Bell", "Brown", "Clark", "Cox", "King", "Lee", "Moore", "Ross", "Smith", "Wong").to[Vector]
    val surnamesSize = surnames.size

//    def order = firstNamesSize * surnamesSize / 10
    def order = 5
    def degrees = new NodeDegreeRange(2, order - 2)
  }

  object NodeObject {
    import ClientData._
    private val r = new scala.util.Random
    def drawFirstName: String = firstNames(r.nextInt(firstNamesSize))
    def drawSurame: String = surnames(r.nextInt(surnamesSize))
    def drawName: String = s"$drawFirstName $drawSurame"
    def drawBalance = 0 + r.nextInt(( 100000 - 0) + 1)
  }

  def generateGraph(initNodeId : Int, subGraphId : Int): (Graph[Node, UnDiEdge], Int) = {
    var nodeId = initNodeId
    val randomMixedGraph = RandomGraph[Node,UnDiEdge,Graph](
        Graph, new RandomGraph.Metrics[Node] {
          val order = ClientData.order
          val nodeDegrees = ClientData.degrees
          def nodeGen: Node = {
            val x = Node(nodeId, NodeObject.drawName, NodeObject.drawBalance, subGraphId)
            nodeId += 1
            return x
          }
        },
        Set(UnDiEdge, LDiEdge)
      )
    val mixedGraph = randomMixedGraph.draw
    return (mixedGraph, nodeId)
  }
}
