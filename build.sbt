name := "flatgraph-example-project"

ThisBuild/organization := "io.joern"
ThisBuild/scalaVersion := "3.4.2"

// run `./updateDependencies.sh` to update this version
// this is read by project/plugins.sbt
val flatgraphVersion = "0.0.83"

lazy val schema = project.in(file("schema"))
  .enablePlugins(FlatgraphCodegenSbtPlugin)
  .settings(
    libraryDependencies += "io.joern" %% "flatgraph-domain-classes-generator" % flatgraphVersion,
    generateDomainClasses/classWithSchema := "testdomains.Simple$", // `$` for the companion object
    generateDomainClasses/fieldName := "schema",
    generateDomainClasses/outputDir := (ThisBuild / baseDirectory).value / generatedDomainClassesDirName,
  )

lazy val domainClasses = project.in(file("domain-classes"))
  .settings(
    libraryDependencies += "io.joern" %% "flatgraph-help" % flatgraphVersion,
    Compile/unmanagedSourceDirectories += (ThisBuild / baseDirectory).value / generatedDomainClassesDirName,
    Compile/compile := (Compile/compile).dependsOn(schema/Compile/generateDomainClasses).value,
  )

lazy val tests = project.in(file("tests"))
  .dependsOn(domainClasses)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.slf4j" % "slf4j-simple" % "2.0.13" % Test,
    )
  )

val generatedDomainClassesDirName = "domain-classes/src/main/scala/generated"

Global / onChangedBuildSource := ReloadOnSourceChanges
