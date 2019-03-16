package core

import java.io._

// create a serializable Stock class
@SerialVersionUID(123L)
class Actor(var symbol: String, var price: BigDecimal) extends Serializable {
  override def toString = f"$symbol%s is ${price.toDouble}%.2f"
}

object SerializationDemo extends App {

  // (1) create a Stock instance
  val nflx = new Actor("NFLX", BigDecimal(85.00))

  // (2) write the instance out to a file
  val oos = new ObjectOutputStream(new FileOutputStream("/tmp/nflx"))
  oos.writeObject(nflx)
  oos.close

  // (3) read the object back in
  val ois = new ObjectInputStream(new FileInputStream("/tmp/nflx"))
  val stock = ois.readObject.asInstanceOf[Actor]
  ois.close

  // (4) print the object that was read back in
  println(stock)

}
