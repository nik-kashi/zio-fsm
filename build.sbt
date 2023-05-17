ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "fsm-showcase"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "dev.zio" %% "zio-actors" % "0.1.0"
libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.13"