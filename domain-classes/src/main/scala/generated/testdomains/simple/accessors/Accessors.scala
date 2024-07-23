package testdomains.simple.accessors
import testdomains.simple.nodes
import scala.collection.immutable.IndexedSeq

/** not supposed to be used directly by users, hence the `bootstrap` in the name */
object languagebootstrap extends ConcreteStoredConversions

object Accessors {
  /* accessors for concrete stored nodes start */
  final class AccessPropertyDescription(val node: nodes.StoredNode) extends AnyVal {
    def description: Option[String] = flatgraph.Accessors.getNodePropertyOption[String](node.graph, node.nodeKind, 0, node.seq)
  }
  final class AccessPropertyName(val node: nodes.StoredNode) extends AnyVal {
    def name: String = flatgraph.Accessors.getNodePropertySingle(node.graph, node.nodeKind, 1, node.seq(), "<empty>": String)
  }
  final class AccessPropertyOrder(val node: nodes.StoredNode) extends AnyVal {
    def order: Int = flatgraph.Accessors.getNodePropertySingle(node.graph, node.nodeKind, 2, node.seq(), 1: Int)
  }
  final class AccessPropertyStringList(val node: nodes.StoredNode) extends AnyVal {
    def stringList: IndexedSeq[String] = flatgraph.Accessors.getNodePropertyMulti[String](node.graph, node.nodeKind, 3, node.seq)
  }
  /* accessors for concrete stored nodes end */

  /* accessors for base nodes start */
  final class AccessThingBase(val node: nodes.ThingBase) extends AnyVal {
    def description: Option[String] = node match {
      case stored: nodes.StoredNode => new AccessPropertyDescription(stored).description
      case newNode: nodes.NewThing  => newNode.description
    }
    def name: String = node match {
      case stored: nodes.StoredNode => new AccessPropertyName(stored).name
      case newNode: nodes.NewThing  => newNode.name
    }
    def order: Int = node match {
      case stored: nodes.StoredNode => new AccessPropertyOrder(stored).order
      case newNode: nodes.NewThing  => newNode.order
    }
    def stringList: IndexedSeq[String] = node match {
      case stored: nodes.StoredNode => new AccessPropertyStringList(stored).stringList
      case newNode: nodes.NewThing  => newNode.stringList
    }
  }
  /* accessors for base nodes end */
}

import Accessors.*
trait ConcreteStoredConversions extends ConcreteBaseConversions {
  implicit def accessPropertyDescription(node: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]): AccessPropertyDescription =
    new AccessPropertyDescription(node)
  implicit def accessPropertyName(node: nodes.StoredNode & nodes.StaticType[nodes.HasNameEMT]): AccessPropertyName = new AccessPropertyName(
    node
  )
  implicit def accessPropertyOrder(node: nodes.StoredNode & nodes.StaticType[nodes.HasOrderEMT]): AccessPropertyOrder =
    new AccessPropertyOrder(node)
  implicit def accessPropertyStringList(node: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]): AccessPropertyStringList =
    new AccessPropertyStringList(node)
}

trait ConcreteBaseConversions {
  implicit def accessThingbase(node: nodes.ThingBase): AccessThingBase = new AccessThingBase(node)
}
