name := """scalaplay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(restfulapi,util).aggregate(restfulapi,util)
scalaVersion := "2.11.7"


lazy val restfulapi = (project in file("modules/apirest")).enablePlugins(PlayScala).dependsOn(util).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq(
    cache,
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  )
)


lazy val util = (project in file("modules/dbmodule")).settings(scalaVersion:="2.11.7")
/*
libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,"com.typesafe.play" %% "play" % "2.3.10",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)*/