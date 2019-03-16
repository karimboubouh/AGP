package graph

import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.generator.{NodeDegreeRange, RandomGraph}
import scalax.collection.mutable.Graph
import scalax.collection.edge.LDiEdge


class Client{
  var counter = 0

  object ClientData {
    val firstNames = Set("Alen", "Alice", "Bob", "Jack", "Jim", "Joe", "Kate", "Leo", "Tim", "Tom").to[Vector]
    val firstNamesSize = firstNames.size

    val surnames = Set("Bell", "Brown", "Clark", "Cox", "King", "Lee", "Moore", "Ross", "Smith", "Wong").to[Vector]
    val surnamesSize = surnames.size

    def order = firstNamesSize * surnamesSize / 10
    def degrees = new NodeDegreeRange(2, order - 2)
  }

  case class Node(nodeId: Int, name: String, balance: Int, subGraphId: Int)

  object Node {
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
            nodeId += 1
            Node(nodeId, Node.drawName, Node.drawBalance, subGraphId)
          }
        },
        Set(UnDiEdge, LDiEdge)
      )
    val mixedGraph = randomMixedGraph.draw
    println(mixedGraph.nodes)

    return (mixedGraph, nodeId)
  }
}
