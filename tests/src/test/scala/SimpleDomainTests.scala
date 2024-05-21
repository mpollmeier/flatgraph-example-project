import flatgraph.help.Table.AvailableWidthProvider
import flatgraph.help.{DocSearchPackages, Table}
import flatgraph.{DiffGraphApplier, GNode}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import testdomains.simple.SimpleDomain
import testdomains.simple.Language.*
import testdomains.simple.edges.ConnectedTo
import testdomains.simple.nodes.{NewThing, Thing}

import scala.collection.mutable

/** Demonstrates usage of generated domain classes for `Simple` domain based on schema in `testdomains.Simple.schema`
 */
class SimpleDomainTests extends AnyWordSpec {

  /* let's create a simple graph that looks like a line, the nodes have the following `name` properties:
   * L2 <- L1 <- Center -> R1 -> R2 -> R3
   * */
  val simpleDomain = {
    // we use the generated `NewThing` api to create new nodes
    // these do not belong to any graph just yet
    val center = NewThing().name("Center")
    val l1     = NewThing().name("L1")
    val l2     = NewThing().name("L2")
    val r1     = NewThing().name("R1").order(2)
    val r2     = NewThing().name("R2").order(2)
    val r3     = NewThing().name("R3").order(2)

    // Now we add all these nodes and their connections to the graph using a DiffGraph (hidden in some convenience API).
    // Note that nodes are added implicitly, since they're referenced in the edges.
    // To add an unconnected node, simply invoke `.addNode`
    SimpleDomain.from(
      _.addEdge(center, l1, ConnectedTo.Label)
      .addEdge(l1, l2, ConnectedTo.Label)
      .addEdge(center, r1, ConnectedTo.Label)
      .addEdge(r1, r2, ConnectedTo.Label)
      .addEdge(r2, r3, ConnectedTo.Label)
    )
  }

  "basic api and steps" in {
    simpleDomain.thing.length shouldBe 6

    val center = simpleDomain.thing.name("Center").next()
    center.order shouldBe 1 // the default value for `order`
    center.connectedTo.name.sorted shouldBe List("L1", "R1")

    // filter by properties: for string properties you can pass regular expressions, for numbers there are `Gte` steps etc.
    simpleDomain.thing.name("R.*").name.l shouldBe List("R1", "R2", "R3")
    simpleDomain.thing.order(2).name.l shouldBe List("R1", "R2", "R3")
    simpleDomain.thing.orderGte(1).size shouldBe 6

    // TODO where step
    // TODO other basic steps - go through api
  }

  "repeat steps" in {
    def center = simpleDomain.thing.name("Center")

    // by default we return only the final elements (if any)
    center.repeat(_.connectedTo)(_.maxDepth(2)).name.sorted shouldBe List("L2", "R2")
    center.repeat(_.connectedTo).size shouldBe 0

    // use `emit` modifier to control what's emitted along the way
    center.repeat(_.connectedTo)(_.maxDepth(2).emit).name.l shouldBe List("Center", "L1", "L2", "R1", "R2")

    // defaults to depth first search (DFS), but can also do breadth first search (BFS)
    center.repeat(_.connectedTo)(_.maxDepth(2).emit.breadthFirstSearch).name.l shouldBe List("Center", "L1", "R1", "L2", "R2")

    // repeat/until
    center.repeat(_.connectedTo)(_.until(_.name(".*2")).emit).name.l shouldBe List("Center", "L1", "L2", "R1", "R2")

  }

  "generic graph api" in {
    simpleDomain.graph.nodeCount shouldBe 6
    simpleDomain.graph.edgeCount shouldBe 5
    simpleDomain.graph.nodes().label.toSet shouldBe Set(Thing.Label)

    val center = simpleDomain.graph.nodes().filter(_.property(Thing.PropertyKeys.Name) == "Center").next()
    center.outE.size shouldBe 2
    center.inE.size shouldBe 0
    center.out(ConnectedTo.Label).property(Thing.PropertyKeys.Name).sorted shouldBe List("L1", "R1")
  }

  // TODO path step - including repeat path:
  //    "work in combination with path" in {
  //      centerTrav.enablePathTracking
  //        .repeat(_.out)(_.until(_.property(StringMandatory).filter(_ == "R2")).maxDepth(2))
  //        .path
  //        .filter(_.last == r2)
  //        .l shouldBe Seq(Vector(center, r1, r2))
  //    }

  // TODO algorithms demo
  // TODO link to flatgraph traversaltests

}
