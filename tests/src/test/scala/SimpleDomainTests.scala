import flatgraph.algorithm.PathFinder
import flatgraph.algorithm.PathFinder.Path
import flatgraph.help.DocSearchPackages
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import testdomains.simple.*
import testdomains.simple.language.*
import testdomains.simple.edges.ConnectedTo
import testdomains.simple.nodes.{NewThing, Thing}

/**
 * Demonstrates usage of generated domain specific graph api our `SimpleDomain` based on schema
 * in `testdomains.Simple.schema`, including repeat and path-aware steps.
 * We also show some some generic graph steps that you can use independently of your domain.
 *
 * For more and more detailed usage take a look at e.g.
 * https://github.com/joernio/flatgraph/tree/master/tests/src/test/scala/flatgraph
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

    // where step filters by a a traversal
    simpleDomain.thing.where(_.connectedTo.name("R3")).name.l shouldBe List("R2")
    // TODO other basic steps - go through api
  }

  "repeat step" in {
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

  "group/groupCount steps" in {
    // which label occurs how often?
    simpleDomain.all.label.groupCount shouldBe Map("thing" -> 6)

    // groupCount length of `name` property
    simpleDomain.thing.groupCount(_.name.length) shouldBe Map(
      2 -> 5, // 5 nodes have a name property of length 2: L1, L2, R1, R2
      6 -> 1  // 1 node has a name property of length 6: Center
    )

    // group the node names by their number of outgoing edges
    simpleDomain.thing.groupMap(_.connectedTo.size)(_.name) shouldBe Map(
      0 -> List("L2", "R3"), // our outermost nodes naturally don't have outgoing edges
      1 -> List("L1", "R1", "R2"),
      2 -> List("Center") // center node is connected to L1 and R1
    )
  }

  "path tracking with step" in {
    def center = simpleDomain.thing.name("Center")

    center
      .enablePathTracking
      .connectedTo
      .connectedTo
      .path.l.map { path =>
      path.map { case t: Thing => t.name }
    } shouldBe List(
      Vector("Center", "L1", "L2"),
      Vector("Center", "R1", "R2")
    )

    // also works with `repeat` step
    center
      .enablePathTracking
      .repeat(_.connectedTo)(_.maxDepth(2))
      .path.l.map { path =>
        path.map { case t: Thing => t.name }
      } shouldBe List(
      Vector("Center", "L1", "L2"),
      Vector("Center", "R1", "R2")
    )
  }

  "path finder" in {
    val Seq(center, l1, l2, r1, r2, r3) = simpleDomain.thing.sortBy(_.name).l
    PathFinder(l1, r3) shouldBe Seq(Path(Seq(l1, center, r1, r2, r3)))
  }

  "generic graph api" in {
    // these work irrespective of your domain
    simpleDomain.graph.nodeCount shouldBe 6
    simpleDomain.graph.edgeCount shouldBe 5
    simpleDomain.graph.nodes().label.toSet shouldBe Set(Thing.Label)

    val center = simpleDomain.graph.nodes().filter(_.propertyOption("name") == Some("Center")).next()
    center.outE.size shouldBe 2
    center.inE.size shouldBe 0
    center.out(ConnectedTo.Label).property[String]("name").sorted shouldBe List("L1", "R1")
  }
}
