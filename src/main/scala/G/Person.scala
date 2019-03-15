package G

import scalax.collection.GraphEdge.UnDiEdge
import scalax.collection.generator.{NodeDegreeRange, RandomGraph}
import scalax.collection.mutable.Graph


class Person{
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
def generateGraph(): Unit={
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
  println(mixedGraph)
}
}
