inThisBuild(Seq(
  name := "rec",
  version := "0.0.0",
  organization := "de.lolhens",

  scalaVersion := "2.12.4",

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
))


name := (name in ThisBuild).value

lazy val settings = Seq(
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val root = project.in(file("."))
  .settings(settings: _*)
  .settings(publishArtifact := false)
  .aggregate(macros)

lazy val base = project.in(file("base"))

lazy val macros = project.in(file("macros"))
  .settings(settings: _*)
  .dependsOn(base)

lazy val test = project.in(file("test"))
  .dependsOn(macros)
