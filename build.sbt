name := """scalaplay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(restfulapi,util).aggregate(restfulapi,util)
scalaVersion := "2.11.7"

/**
  * .dependsOn(util). will let us use element from dbmodule into apirestmodule. Specifically some element and structure
  * of the data model.
  *
  */

lazy val restfulapi = (project in file("modules/apirest")).enablePlugins(PlayScala).dependsOn(util).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq(
    cache,
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
    "org.mockito" % "mockito-core" % "2.5.0" % "test"
  )
)

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"

lazy val util = (project in file("modules/dbmodule")).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq( "com.typesafe.play" % "play-json_2.11" % "2.5.10",
    scalatest % "test"
  )
)
