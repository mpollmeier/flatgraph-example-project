name := "flatgraph-example-project"
ThisBuild/organization := "io.joern"
ThisBuild/scalaVersion := "3.4.2"

// n.b.1: you can manually update the version or run ./updateDependencies.sh
// n.b.2: this is read by project/plugins.sbt
val flatgraphVersion = "0.0.60"

lazy val schema = project
  .in(file("schema"))
  .enablePlugins(FlatgraphCodegenSbtPlugin)
  .settings(
    libraryDependencies += "io.joern" %% "flatgraph-domain-classes-generator" % flatgraphVersion,
    generateDomainClasses/classWithSchema := "testdomains.Simple$", // `$` for the companion object
    generateDomainClasses/fieldName := "schema",
    generateDomainClasses/outputDir := (ThisBuild / baseDirectory).value / generatedDomainClassesDirName,
  )

lazy val domainClasses = project
  .in(file("domain-classes"))
  .settings(
    libraryDependencies += "io.joern" %% "flatgraph-core" % flatgraphVersion,
    Compile/unmanagedSourceDirectories += (ThisBuild / baseDirectory).value / generatedDomainClassesDirName,
    Compile/compile := (Compile/compile).dependsOn(schema/Compile/generateDomainClasses).value,

    /* generated sources occasionally have some warnings..
    * we're trying to minimise them on a best effort basis, but don't want
    * to fail the build because of them */
    Compile / scalacOptions --= Seq("-Wconf:cat=deprecation:w,any:e", "-Wunused", "-Ywarn-unused"),
  )

lazy val tests = project
  .in(file("tests"))
  .dependsOn(domainClasses)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "org.slf4j" % "slf4j-simple" % "2.0.7" % Test,
    )
  )

val generatedDomainClassesDirName = "domain-classes/src/main/scala/generated"
