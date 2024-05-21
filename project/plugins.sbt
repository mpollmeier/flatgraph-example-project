lazy val flatgraphVersion = {
  val versionRegexp = s""".*val flatgraphVersion[ ]+=[ ]?"(.*?)"""".r
  scala.io.Source
    .fromFile("build.sbt")
    .getLines
    .filter(_.startsWith("val flatgraphVersion"))
    .collect { case versionRegexp(version) => version }
    .next()
}

addSbtPlugin("io.joern" % "sbt-flatgraph" % flatgraphVersion)
