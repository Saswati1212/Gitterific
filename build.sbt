name := """Gitterific"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaWs,
  ehcache,
)



libraryDependencies += "org.mockito" % "mockito-core" % "2.22.0" % "test"

libraryDependencies += "junit" % "junit" % "4.13.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.14"




dependencyOverrides ++= Seq( "org.jacoco" % "org.jacoco.core" % "0.8.2", "org.jacoco" % "org.jacoco.report" % "0.8.2",
  "org.assertj" % "assertj-core" % "3.21.0", "org.assertj" % "assertj-swing" % "3.21.0",
  "org.assertj" % "assertj-junit" % "3.21.0"
)
