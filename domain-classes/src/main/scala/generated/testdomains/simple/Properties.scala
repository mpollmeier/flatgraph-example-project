package testdomains.simple

object Properties {
  val Description = flatgraph.OptionalPropertyKey[String](kind = 0, name = "description")

  val Name = flatgraph.SinglePropertyKey[String](kind = 1, name = "name", default = "<empty>")

  val Order = flatgraph.SinglePropertyKey[Int](kind = 2, name = "order", default = 1: Int)

  val StringList = flatgraph.MultiPropertyKey[String](kind = 3, name = "string_list")
}
