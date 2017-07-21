organization := "com.inkenkun.x1"

name := "scala-monix"

version := "1.0"

scalaVersion := "2.12.2"

scalacOptions ++= Seq( "-deprecation", "-encoding", "UTF-8", "-target:jvm-1.8" )

val http4sVersion = "0.15.13a"

libraryDependencies ++= Seq(
  "io.monix" %% "monix" % "2.3.0",
  "io.monix" %% "monix-cats" % "2.3.0",
  "org.http4s"     %% "http4s-blaze-server"  % http4sVersion,
  "org.http4s"     %% "http4s-dsl"           % http4sVersion,
  "org.typelevel"  %% "cats"                 % "0.9.0",
  "org.scalatest"    %% "scalatest" % "3.0.1" % "test"
)
