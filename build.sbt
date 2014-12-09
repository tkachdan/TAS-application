name := """hib-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

val appDependencies = Seq(
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.apache.directory.api" % "api-viewAll" % "1.0.0-M14",
  "org.hibernate" % "hibernate-core" % "4.2.3.Final"
)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)


libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"


