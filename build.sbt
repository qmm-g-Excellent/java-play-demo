name := "java-play-demo"
organization := "play"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    guice,
    evolutions,
    ws,
    "org.apache.httpcomponents" % "httpclient" % "4.5.3",
    "redis.clients" % "jedis" % "2.9.0",
    "org.projectlombok" % "lombok" % "1.16.16"
  )
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .enablePlugins(PlayJava, PlayEbean)