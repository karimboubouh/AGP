# AGP (Actor Graph Persistence)

Implementation of a distributed CoreGraph API using PersistentActors: 

Akka persistence enables stateful actors to persist their state so that it can be recovered when an actor is restarted, this can happen either through a JVM crashing, or a manual stop-start by a supervisor. The key concept behind the implementation is that only the states received by the actors are persisted, not the actual state of the actor. The states persisted are journalled messages which are then replayed back to the actors by appending to storage which allows for very high transaction rates and efficient replication. A stateful actor is then recovered by replaying the stored states from the snapshots to the actor, allowing it to rebuild its new state.



# How it works

Graph protocol

The CoreGraph adopts the concept of Undirected relationship to store informamtion between node, all edge are discovered through shared informamtion by nodes,  



PersistentActor: Is a persistent, stateful actor that is able to persist events to a journal and can react to them in a thread-safe manner. It can be used to implement both command as well as event sourced actors. When a persistent actor is started or restarted, journaled messages are replayed to that actor so that it can recover its state from these messages.



Serializing an actor
// create a serializable Stock class
@SerialVersionUID(123L)
class Actor(var symbol: String, var price: BigDecimal) extends Serializable {
  override def toString = f"$symbol%s is ${price.toDouble}%.2f"
  

Ochestrator

DirectedGraphApplication
 






