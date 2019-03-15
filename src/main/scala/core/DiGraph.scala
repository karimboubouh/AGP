/*
package core

import scalax.collection.Graph
import scalax.collection.GraphEdge.{DiEdge, EdgeCopy, ExtendedKey, NodeProduct}
import scalax.collection.GraphPredef._

case class Edge[+Node](fromNode: Node, toNode: Node, labelNode: String)
  extends DiEdge[Node](NodeProduct(fromNode, toNode))
    with    ExtendedKey[Node]
    with    EdgeCopy[Edge]
    with    OuterEdge[Node,Edge]
{
  private def this(nodes: Product, labelNode: String) {
    this(nodes.productElement(0).asInstanceOf[Node],
      nodes.productElement(1).asInstanceOf[Node], labelNode)
  }
  def keyAttributes = Seq(labelNode)
  override def copy[NN](newNodes: Product) = new Edge[NN](newNodes, labelNode)
  override protected def attributesToString = s" ($labelNode)"
}

object Edge {
  implicit final class ImplicitEdge[A <: Node](val e: DiEdge[A]) extends AnyVal {
    def ## (labelNode: String) = new Edge[A](e.source, e.target, labelNode)
  }
}

class DiGraph {

  val g = Graph[Node, Edge]()

  def addNode(node: Node) : Unit = {
    g + node
  }

  def getNode(node: Node) : Unit = {
    val a = g get node
    println(a)
    return a
  }

}

object Demo {
  def main(args: Array[String]) {
    val graph = new DiGraph()

    val x = new Node(1, "hello")
    val y = new Node(2, "world")
    graph.addNode(x)
    graph.addNode(y)

    println("-----------------")

//    println(graph.getNode(x))
//    println(graph.getNode(y))

    // val g = Graph(n1 ~ n2)
     println(graph.g.nodes)

  }




}
*/

import scalax.collection.Graph
import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.generator.RandomGraph
import scalax.collection.generator.parameters.NodeDegreeRange

val predefined = RandomGraph.tinyConnectedIntDi(Graph)
val tinyGraph = predefined.draw // Graph[Int,DiEdge]

object sparse_1000_Int extends RandomGraph.IntFactory {
  val order = 1000
  val nodeDegrees = NodeDegreeRange(1,10)
  override def connected = false
}
val randomSparse = RandomGraph[Int,UnDiEdge,Graph](
  Graph, sparse_1000_Int, Set(UnDiEdge))
val sparseGraph = randomSparse.draw // Graph[Int,UnDiEdge]

object PersonData {
  val firstNames = Set(
    "Alen", "Alice", "Bob", "Jack", "Jim",
    "Joe", "Kate", "Leo", "Tim", "Tom").to[Vector]
  val firstNamesSize = firstNames.size

  val surnames = Set(
    "Bell", "Brown", "Clark", "Cox", "King",
    "Lee", "Moore", "Ross", "Smith", "Wong").to[Vector]
  val surnamesSize = surnames.size

  def order = firstNamesSize * surnamesSize / 10
  def degrees = new NodeDegreeRange(2, order - 2)
  val maxYearOfBirth = 2010
}

import scalax.collection.edge.LDiEdge

case class Person(name: String, yearOfBirth: Int)
object Person {
  import PersonData._
  private val r = new scala.util.Random

  def drawFirstName: String = firstNames(r.nextInt(firstNamesSize))
  def drawSurame: String = surnames(r.nextInt(surnamesSize))

  def drawName: String = s"$drawFirstName, $drawSurame"

  def drawYearOfBirth = maxYearOfBirth - r.nextInt(100)
}

val randomMixedGraph =
  RandomGraph[Person,UnDiEdge,Graph](
    Graph, new RandomGraph.Metrics[Person] {
      val order = PersonData.order
      val nodeDegrees = PersonData.degrees
      def nodeGen: Person = Person(Person.drawName, Person.drawYearOfBirth)
    },
    Set(UnDiEdge, LDiEdge)
  )
val mixedGraph = randomMixedGraph.draw

