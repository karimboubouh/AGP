lazy val commonSettings = Seq(
  version := "1.0",
  name := "AGP",
  organization := "UM6P",
  scalaVersion := "2.12.8"
)

lazy val akkaVersion = "2.5.21"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
  "org.scala-graph" %% "graph-core" % "1.12.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "io.kamon" % "sigar-loader" % "1.6.6-rev002"
)