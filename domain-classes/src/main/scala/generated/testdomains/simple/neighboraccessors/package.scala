package testdomains.simple

import flatgraph.traversal.language.*
import testdomains.simple.nodes

package object neighboraccessors {
  object Lang extends Conversions

  trait Conversions {
    implicit def accessNeighborsForThing(node: nodes.Thing): AccessNeighborsForThing =
      new AccessNeighborsForThing(node)

    implicit def accessNeighborsForThingTraversal(traversal: IterableOnce[nodes.Thing]): AccessNeighborsForThingTraversal =
      new AccessNeighborsForThingTraversal(traversal.iterator)
  }
}
