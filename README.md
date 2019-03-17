# AGP (Actor Graph Persistence)

Implementation of a distributed CoreGraph API using PersistentActors: 

Akka persistence enables stateful actors to persist their state so that it can be recovered when an actor is restarted, this can happen either through a JVM crashing, or a manual stop-start by a supervisor. The key concept behind the implementation is that only the states received by the actors are persisted, not the actual state of the actor. The states persisted are journalled messages which are then replayed back to the actors by appending to storage which allows for very high transaction rates and efficient replication. A stateful actor is then recovered by replaying the stored states from the snapshots to the actor, allowing it to rebuild its new state.



# How it works

Graph protocol

The CoreGraph adopts the concept of directed relationship to store informamtion between nodes, all edge are discovered through shared informamtion by nodes, the entire graph is seperated into subgrapgh to represent the entire graph. Each subraph is represented by a PersistenActor which is then controlled by the Orchestrator.

<img width="527" alt="Screenshot 2019-03-17 at 17 59 02" src="https://user-images.githubusercontent.com/27771844/54494947-63244280-48d7-11e9-89b9-2c3955a38a89.png">


<img width="527" alt="Screenshot 2019-03-17 at 17 54 02" src="https://user-images.githubusercontent.com/27771844/54494943-628bac00-48d7-11e9-98c1-f95965a48263.png">



PersistentActor: Is a persistent, stateful actor that is able to persist events to a journal and can react to them in a thread-safe manner. It can be used to implement both command as well as event sourced actors. When a persistent actor is started or restarted, journaled messages are replayed to that actor so that it can recover its state from these messages.

<img width="527" alt="Screenshot 2019-03-17 at 17 58 10" src="https://user-images.githubusercontent.com/27771844/54494946-63244280-48d7-11e9-8abc-cec0232df411.png">



Serializing an actor

<img width="527" alt="Screenshot 2019-03-17 at 17 41 22" src="https://user-images.githubusercontent.com/27771844/54494941-61f31580-48d7-11e9-8d90-c6add57bd3bb.png">


  

Ochestrator is like the master that controls the entire graph, this maintains the mapping between the workers and the subgraphs. The subgraphs represents the part of the entire graph. 
the master 

<img width="527" alt="Screenshot 2019-03-17 at 17 57 34" src="https://user-images.githubusercontent.com/27771844/54494945-628bac00-48d7-11e9-9e4f-0a5fbcd87420.png">




<img width="527" alt="Screenshot 2019-03-17 at 17 56 13" src="https://user-images.githubusercontent.com/27771844/54494944-628bac00-48d7-11e9-8235-f32f54fc1440.png">



DirectedGraphApplication

<img width="527" alt="Screenshot 2019-03-17 at 17 51 57" src="https://user-images.githubusercontent.com/27771844/54494942-628bac00-48d7-11e9-9556-5628b17bccaf.png">


 






