name := """scalaplay"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(restfulapi,util).aggregate(restfulapi,util)
scalaVersion := "2.11.7"
lazy val restfulapi = (project in file("modules/apirest")).enablePlugins(PlayScala).dependsOn(util).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq(
    cache,filters,
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
    "org.mockito" % "mockito-core" % "2.5.0" % "test",
    "com.nulab-inc" %% "play2-oauth2-provider" % "1.2.0"
  )
)

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"

lazy val util = (project in file("modules/dbmodule")).settings(scalaVersion:="2.11.7",
  libraryDependencies ++= Seq( "com.typesafe.play" % "play-json_2.11" % "2.5.10",
    scalatest % "test"
  )
)
lazy val swaggerapidoc = (project in file("modules/apidoc"))