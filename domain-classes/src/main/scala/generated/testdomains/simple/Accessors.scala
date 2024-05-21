package testdomains.simple.accessors
import testdomains.simple.nodes
import scala.collection.immutable.IndexedSeq

object Lang extends ConcreteStoredConversions

object Accessors {
  /* accessors for concrete stored nodes start */
  final class Access_Property_description(val node: nodes.StoredNode) extends AnyVal {
    def description: Option[String] = flatgraph.Accessors.getNodePropertyOption[String](node.graph, node.nodeKind, 0, node.seq)
  }
  final class Access_Property_name(val node: nodes.StoredNode) extends AnyVal {
    def name: String = flatgraph.Accessors.getNodePropertySingle(node.graph, node.nodeKind, 1, node.seq(), "<empty>": String)
  }
  final class Access_Property_order(val node: nodes.StoredNode) extends AnyVal {
    def order: Int = flatgraph.Accessors.getNodePropertySingle(node.graph, node.nodeKind, 2, node.seq(), 1: Int)
  }
  final class Access_Property_string_list(val node: nodes.StoredNode) extends AnyVal {
    def stringList: IndexedSeq[String] = flatgraph.Accessors.getNodePropertyMulti[String](node.graph, node.nodeKind, 3, node.seq)
  }
  /* accessors for concrete stored nodes end */

  /* accessors for base nodes start */
  final class Access_ThingBase(val node: nodes.ThingBase) extends AnyVal {
    def description: Option[String] = node match {
      case stored: nodes.StoredNode => new Access_Property_description(stored).description
      case newNode: nodes.NewThing  => newNode.description
    }
    def name: String = node match {
      case stored: nodes.StoredNode => new Access_Property_name(stored).name
      case newNode: nodes.NewThing  => newNode.name
    }
    def order: Int = node match {
      case stored: nodes.StoredNode => new Access_Property_order(stored).order
      case newNode: nodes.NewThing  => newNode.order
    }
    def stringList: IndexedSeq[String] = node match {
      case stored: nodes.StoredNode => new Access_Property_string_list(stored).stringList
      case newNode: nodes.NewThing  => newNode.stringList
    }
  }
  /* accessors for base nodes end */
}

trait ConcreteStoredConversions extends ConcreteBaseConversions {
  import Accessors.*
  implicit def accessPropertyDescription(node: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]): Access_Property_description =
    new Access_Property_description(node)
  implicit def accessPropertyName(node: nodes.StoredNode & nodes.StaticType[nodes.HasNameEMT]): Access_Property_name =
    new Access_Property_name(node)
  implicit def accessPropertyOrder(node: nodes.StoredNode & nodes.StaticType[nodes.HasOrderEMT]): Access_Property_order =
    new Access_Property_order(node)
  implicit def accessPropertyStringList(node: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]): Access_Property_string_list =
    new Access_Property_string_list(node)
}

trait ConcreteBaseConversions {
  import Accessors.*
  implicit def access_ThingBase(node: nodes.ThingBase): Access_ThingBase = new Access_ThingBase(node)
}
