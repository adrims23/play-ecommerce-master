name := """play-java-docker-k8s-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = project
   .in(file("."))
   .enablePlugins(PlayJava)
   .enablePlugins(JavaAppPackaging)

scalaVersion := "2.12.2"


   libraryDependencies +="com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0"
libraryDependencies += "com.google.guava" % "guava" % "18.0"
libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += javaWs


// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play" % "2.6.12"

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Which docker repository to publish to
dockerRepository := Some("samplegroup")
// Update the latest tag when publishing
dockerUpdateLatest := true
