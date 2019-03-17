# AGP (Actor Graph Persistence)

Implementation of a distributed CoreGraph API using PersistentActors: 

Akka persistence is a concept that enables stateful actors to persist their state so that it can be recovered when an actor is restarted, this can happen either through a JVM crashing, or a manual stop-start by a supervisor. The key idea behind the implementation is that actors are able to recover the states of the system even after a crash A stateful actor is then recovered by replaying the stored states from the snapshots to the actor, allowing it to rebuild its new state.



# How it works

Graph protocol

The CoreGraph adopts the concept of directed relationship to store informamtion between nodes, all edge are discovered through shared informamtion by nodes, the entire graph is seperated into subgrapgh to represent the system. Each subgraph is represented by a PersistenActor which is then controlled by the Orchestrator.

<img width="527" alt="Screenshot 2019-03-17 at 17 59 02" src="https://user-images.githubusercontent.com/27771844/54494947-63244280-48d7-11e9-89b9-2c3955a38a89.png">



PersistentActor: Is a persistent, stateful actor that is able to persist events and restore actor states after 20 seconds. After which messages are been replayed to that actor so that it can recover its state from these messages.

<img width="606" alt="Screenshot 2019-03-17 at 19 33 37" src="https://user-images.githubusercontent.com/27771844/54496108-5f96b880-48e3-11e9-87a4-f82344f63891.png">



Serializing a state.
Each actors state is been serialized and saved to disk to keep the information from detoying, when an information is desired, is been fetched from the local disk to be replayed to the actors.

<img width="527" alt="Screenshot 2019-03-17 at 17 41 22" src="https://user-images.githubusercontent.com/27771844/54494941-61f31580-48d7-11e9-8d90-c6add57bd3bb.png">
  

Ochestrator is like the master that controls the entire graph, this maintains the mapping between the workers and the subgraphs. The subgraphs represents the part of the entire graph.

<img width="527" alt="Screenshot 2019-03-17 at 17 57 34" src="https://user-images.githubusercontent.com/27771844/54494945-628bac00-48d7-11e9-9e4f-0a5fbcd87420.png">




<img width="527" alt="Screenshot 2019-03-17 at 17 56 13" src="https://user-images.githubusercontent.com/27771844/54494944-628bac00-48d7-11e9-8235-f32f54fc1440.png">


# How it runs
DGraphApp.
This represents the main object that runs the application. When initiated, it creates an instance of the ochestrator, and a number of workers (line 19: 100) depending on the graph size.  

<img width="527" alt="Screenshot 2019-03-17 at 19 21 31" src="https://user-images.githubusercontent.com/27771844/54495969-953aa200-48e1-11e9-93ad-14970d742ced.png">







# Team Members: 
BOUBOUH Karim
BOUAYAD Abdelhak
BENTACHFINE Ilyas
ABDULSALAM Yunusa Simpa




 






