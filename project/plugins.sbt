logLevel := Level.Warn

resolvers ++= Seq(
  "lolhens-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
  Resolver.url("lolhens-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.2")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")

addSbtPlugin("com.lucidchart" % "sbt-cross" % "3.0")

//addSbtPlugin("org.scala-native" % "sbt-crossproject" % "0.2.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.20")

addSbtPlugin("de.lolhens.sbt" % "sbt-no-snapshots" % "0.1.0")
