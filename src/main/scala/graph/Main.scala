package graph

object Main extends App {
  val person = new Client()
  val x = person.generateGraph(1, 11)

  println(s"Last nodeId : ${x._2} | GraphSet(${x._1.nodes.size})")
}
