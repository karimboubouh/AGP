package cluster

object GWProtocol {
  case class getNode(id:Int)
  case class deleteNode(id:Int)
  case object registerWorker
}
