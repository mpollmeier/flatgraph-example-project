package testdomains

import flatgraph.schema.*
import flatgraph.schema.EdgeType.Cardinality
import flatgraph.schema.Property.ValueType

object Simple {
  val schema: Schema = {
    val builder = new SchemaBuilder(domainShortName = "SimpleDomain", basePackage = "testdomains.simple")

    // properties
    val name        = builder.addProperty("name", ValueType.String).mandatory(default = "<empty>")
    val description = builder.addProperty("description", ValueType.String)
    val stringList  = builder.addProperty("string_list", ValueType.String).asList()
    val order        = builder.addProperty("order", ValueType.Int).mandatory(default = 1)

    val thing = builder
      .addNodeType("thing")
      .addProperty(name)
      .addProperty(description)
      .addProperty(stringList)
      .addProperty(order)

    val connectedTo = builder.addEdgeType("connected_to").withProperty(name)
    thing.addOutEdge(
      edge = connectedTo,
      inNode = thing,
      stepNameOut = "connectedTo",
      stepNameOutDoc = "Connected neighbor thing"
    )

    builder.build
  }
}
