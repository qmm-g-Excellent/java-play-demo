name := """java-play-demo"""
organization := "play"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice,
  "org.apache.httpcomponents"      % "httpclient"                   % "4.5.3",
)
