package testdomains.simple.neighboraccessors

import testdomains.simple.nodes
import testdomains.simple.Language.*

final class AccessNeighborsForThing(val node: nodes.Thing) extends AnyVal {

  /** Traverse to thing via connected_to IN edge.
    */
  def thingViaConnectedToIn: Iterator[nodes.Thing] = connectedToIn.collectAll[nodes.Thing]

  /** Connected neighbor thing Traverse to thing via connected_to OUT edge.
    */
  @deprecated("please use connectedTo instead")
  def thingViaConnectedToOut: Iterator[nodes.Thing] = connectedTo

  /** Connected neighbor thing Traverse to thing via connected_to OUT edge.
    */
  def connectedTo: Iterator[nodes.Thing] = connectedToOut.collectAll[nodes.Thing]

  def connectedToIn: Iterator[nodes.Thing] = node._connectedToIn.cast[nodes.Thing]

  def connectedToOut: Iterator[nodes.Thing] = node._connectedToOut.cast[nodes.Thing]
}

final class AccessNeighborsForThingTraversal(val traversal: Iterator[nodes.Thing]) extends AnyVal {

  /** Traverse to thing via connected_to IN edge.
    */
  def thingViaConnectedToIn: Iterator[nodes.Thing] = traversal.flatMap(_.thingViaConnectedToIn)

  /** Connected neighbor thing Traverse to thing via connected_to OUT edge.
    */
  def connectedTo: Iterator[nodes.Thing] = traversal.flatMap(_.connectedTo)

  /** Connected neighbor thing Traverse to thing via connected_to OUT edge.
    */
  @deprecated("please use connectedTo instead")
  def thingViaConnectedToOut: Iterator[nodes.Thing] = traversal.flatMap(_.thingViaConnectedToOut)

  def connectedToIn: Iterator[nodes.Thing] = traversal.flatMap(_.connectedToIn)

  def connectedToOut: Iterator[nodes.Thing] = traversal.flatMap(_.connectedToOut)
}
