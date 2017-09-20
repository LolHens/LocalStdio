name := (name in ThisBuild).value

inThisBuild(Seq(
  name := "localStdio",
  organization := "org.lolhens",
  version := "0.0.1",

  scalaVersion := "2.12.3",

  externalResolvers := Seq(
    Resolver.defaultLocal,
    "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
    Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq("-Xmax-classfile-name", "127")
))

lazy val root = project.in(file("."))
  .settings(publishArtifact := false)
  .aggregate(
    localStdioJVM_2_11,
    localStdioJVM_2_12,
    //localStdioJVM_2_13,
    localStdioJS_2_11,
    localStdioJS_2_12
  )

lazy val localStdio = crossProject.crossType(CrossType.Full)
  .settings(name := (name in ThisBuild).value)

lazy val localStdioJVM_2_11 = localStdio.jvm.cross("2.11.11").settings(name := (name in ThisBuild).value)
lazy val localStdioJVM_2_12 = localStdio.jvm.cross("2.12.3").settings(name := (name in ThisBuild).value)
//lazy val localStdioJVM_2_13 = localStdio.jvm.cross("2.13.0-M2").settings(name := (name in ThisBuild).value)
lazy val localStdioJS_2_11 = localStdio.js.cross("2.11.11").settings(name := (name in ThisBuild).value)
lazy val localStdioJS_2_12 = localStdio.js.cross("2.12.3").settings(name := (name in ThisBuild).value)
