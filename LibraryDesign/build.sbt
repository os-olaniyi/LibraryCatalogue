import scala.collection.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "LibraryDesign",
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.5.1",
      "org.slf4j" % "slf4j-nop" % "2.0.13",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
      "mysql" % "mysql-connector-java" % "8.0.33",
      //"ch.qos.logback" % "logback-classic" % "1.5.6",
      "com.typesafe" % "config" % "1.4.3",
      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    )
  )
