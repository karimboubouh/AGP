package core

class Node(_id : Int, _value : String) {
  var id = _id
  var value = _value

  //var subGraphId : Int

  def isInSubGraph(subId : Int) : Boolean = {
     true
  }

  override def toString = s"Node($id, $value)"

}
