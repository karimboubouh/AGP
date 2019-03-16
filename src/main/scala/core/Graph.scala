package core

trait Graph {

  def getNode(key : Any) : Node
  def addNode(node : Node) : Any
  def updateNode(key : Any)
  def getSubGraph(key : Any) : List[Node]

}
