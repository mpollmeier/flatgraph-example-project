package testdomains.simple.traversals
import testdomains.simple.nodes

object Lang extends ConcreteStoredConversions

object Accessors {
  import testdomains.simple.accessors.Lang.*

  /* accessors for concrete stored nodes start */
  final class Traversal_Property_description[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]](
    val traversal: Iterator[NodeType]
  ) extends AnyVal {

    /** Traverse to description property */
    def description: Iterator[String] =
      traversal.flatMap(_.description)

    /** Traverse to nodes where the description matches the regular expression `value`
      */
    def description(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        descriptionExact(pattern)
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filter { item =>
          val tmp = item.description; tmp.isDefined && matcher.reset(tmp.get).matches
        }
      }
    }

    /** Traverse to nodes where the description matches at least one of the regular expressions in `values`
      */
    def description(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item =>
        val tmp = item.description; tmp.isDefined && matchers.exists { _.reset(tmp.get).matches }
      }
    }

    /** Traverse to nodes where description matches `value` exactly.
      */
    def descriptionExact(value: String): Iterator[NodeType] = traversal match {
      case init: flatgraph.misc.InitNodeIterator[flatgraph.GNode @unchecked] if init.isVirgin && init.hasNext =>
        val someNode = init.next
        flatgraph.Accessors.getWithInverseIndex(someNode.graph, someNode.nodeKind, 0, value).asInstanceOf[Iterator[NodeType]]
      case _ =>
        traversal.filter { node =>
          val tmp = node.description; tmp.isDefined && tmp.get == value
        }
    }

    /** Traverse to nodes where description matches one of the elements in `values` exactly.
      */
    def descriptionExact(values: String*): Iterator[NodeType] =
      if (values.length == 1) descriptionExact(values.head)
      else {
        val valueSet = values.toSet
        traversal.filter { item =>
          val tmp = item.description; tmp.isDefined && valueSet.contains(tmp.get)
        }
      }

    /** Traverse to nodes where description does not match the regular expression `value`.
      */
    def descriptionNot(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        traversal.filter { node => node.description.isEmpty || node.description.get != pattern }
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filterNot { item =>
          val tmp = item.description; tmp.isDefined && matcher.reset(tmp.get).matches
        }
      }
    }

    /** Traverse to nodes where description does not match any of the regular expressions in `values`.
      */
    def descriptionNot(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filterNot { item =>
        val tmp = item.description; tmp.isDefined && matchers.exists { _.reset(tmp.get).matches }
      }
    }

  }
  final class Traversal_Property_name[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasNameEMT]](val traversal: Iterator[NodeType])
      extends AnyVal {

    /** Traverse to name property */
    def name: Iterator[String] =
      traversal.map(_.name)

    /** Traverse to nodes where the name matches the regular expression `value`
      */
    def name(pattern: String): Iterator[NodeType] =
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        nameExact(pattern)
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filter { item => matcher.reset(item.name).matches }
      }

    /** Traverse to nodes where the name matches at least one of the regular expressions in `values`
      */
    def name(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item => matchers.exists { _.reset(item.name).matches } }
    }

    /** Traverse to nodes where name matches `value` exactly.
      */
    def nameExact(value: String): Iterator[NodeType] = traversal match {
      case init: flatgraph.misc.InitNodeIterator[flatgraph.GNode @unchecked] if init.isVirgin && init.hasNext =>
        val someNode = init.next
        flatgraph.Accessors.getWithInverseIndex(someNode.graph, someNode.nodeKind, 1, value).asInstanceOf[Iterator[NodeType]]
      case _ => traversal.filter { _.name == value }
    }

    /** Traverse to nodes where name matches one of the elements in `values` exactly.
      */
    def nameExact(values: String*): Iterator[NodeType] =
      if (values.length == 1) nameExact(values.head)
      else {
        val valueSet = values.toSet
        traversal.filter { item => valueSet.contains(item.name) }
      }

    /** Traverse to nodes where name does not match the regular expression `value`.
      */
    def nameNot(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        traversal.filter { node => node.name != pattern }
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filterNot { item => matcher.reset(item.name).matches }
      }
    }

    /** Traverse to nodes where name does not match any of the regular expressions in `values`.
      */
    def nameNot(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item => matchers.find { _.reset(item.name).matches }.isEmpty }
    }

  }
  final class Traversal_Property_order[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasOrderEMT]](
    val traversal: Iterator[NodeType]
  ) extends AnyVal {

    /** Traverse to order property */
    def order: Iterator[Int] =
      traversal.map(_.order)

    /** Traverse to nodes where the order equals the given `value`
      */
    def order(value: Int): Iterator[NodeType] =
      traversal.filter { _.order == value }

    /** Traverse to nodes where the order equals at least one of the given `values`
      */
    def order(values: Int*): Iterator[NodeType] = {
      val vset = values.toSet
      traversal.filter { node => vset.contains(node.order) }
    }

    /** Traverse to nodes where the order is not equal to the given `value`
      */
    def orderNot(value: Int): Iterator[NodeType] =
      traversal.filter { _.order != value }

    /** Traverse to nodes where the order is not equal to any of the given `values`
      */
    def orderNot(values: Int*): Iterator[NodeType] = {
      val vset = values.toSet
      traversal.filter { node => !vset.contains(node.order) }
    }

    /** Traverse to nodes where the order is greater than the given `value`
      */
    def orderGt(value: Int): Iterator[NodeType] =
      traversal.filter { _.order > value }

    /** Traverse to nodes where the order is greater than or equal the given `value`
      */
    def orderGte(value: Int): Iterator[NodeType] =
      traversal.filter { _.order >= value }

    /** Traverse to nodes where the order is less than the given `value`
      */
    def orderLt(value: Int): Iterator[NodeType] =
      traversal.filter { _.order < value }

    /** Traverse to nodes where the order is less than or equal the given `value`
      */
    def orderLte(value: Int): Iterator[NodeType] =
      traversal.filter { _.order <= value }

  }
  final class Traversal_Property_string_list[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]](
    val traversal: Iterator[NodeType]
  ) extends AnyVal {

    /** Traverse to stringList property */
    def stringList: Iterator[String] =
      traversal.flatMap(_.stringList)

  }
  /* accessors for concrete stored nodes end */

  /* accessors for base nodes start */
  final class Traversal_ThingBase[NodeType <: nodes.ThingBase](val traversal: Iterator[NodeType]) extends AnyVal {

    /** Traverse to description property */
    def description: Iterator[String] =
      traversal.flatMap(_.description)

    /** Traverse to nodes where the description matches the regular expression `value`
      */
    def description(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        descriptionExact(pattern)
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filter { item =>
          val tmp = item.description; tmp.isDefined && matcher.reset(tmp.get).matches
        }
      }
    }

    /** Traverse to nodes where the description matches at least one of the regular expressions in `values`
      */
    def description(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item =>
        val tmp = item.description; tmp.isDefined && matchers.exists { _.reset(tmp.get).matches }
      }
    }

    /** Traverse to nodes where description matches `value` exactly.
      */
    def descriptionExact(value: String): Iterator[NodeType] = traversal match {
      case init: flatgraph.misc.InitNodeIterator[flatgraph.GNode @unchecked] if init.isVirgin && init.hasNext =>
        val someNode = init.next
        flatgraph.Accessors.getWithInverseIndex(someNode.graph, someNode.nodeKind, 0, value).asInstanceOf[Iterator[NodeType]]
      case _ =>
        traversal.filter { node =>
          val tmp = node.description; tmp.isDefined && tmp.get == value
        }
    }

    /** Traverse to nodes where description matches one of the elements in `values` exactly.
      */
    def descriptionExact(values: String*): Iterator[NodeType] =
      if (values.length == 1) descriptionExact(values.head)
      else {
        val valueSet = values.toSet
        traversal.filter { item =>
          val tmp = item.description; tmp.isDefined && valueSet.contains(tmp.get)
        }
      }

    /** Traverse to nodes where description does not match the regular expression `value`.
      */
    def descriptionNot(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        traversal.filter { node => node.description.isEmpty || node.description.get != pattern }
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filterNot { item =>
          val tmp = item.description; tmp.isDefined && matcher.reset(tmp.get).matches
        }
      }
    }

    /** Traverse to nodes where description does not match any of the regular expressions in `values`.
      */
    def descriptionNot(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filterNot { item =>
        val tmp = item.description; tmp.isDefined && matchers.exists { _.reset(tmp.get).matches }
      }
    }

    /** Traverse to name property */
    def name: Iterator[String] =
      traversal.map(_.name)

    /** Traverse to nodes where the name matches the regular expression `value`
      */
    def name(pattern: String): Iterator[NodeType] =
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        nameExact(pattern)
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filter { item => matcher.reset(item.name).matches }
      }

    /** Traverse to nodes where the name matches at least one of the regular expressions in `values`
      */
    def name(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item => matchers.exists { _.reset(item.name).matches } }
    }

    /** Traverse to nodes where name matches `value` exactly.
      */
    def nameExact(value: String): Iterator[NodeType] = traversal match {
      case init: flatgraph.misc.InitNodeIterator[flatgraph.GNode @unchecked] if init.isVirgin && init.hasNext =>
        val someNode = init.next
        flatgraph.Accessors.getWithInverseIndex(someNode.graph, someNode.nodeKind, 1, value).asInstanceOf[Iterator[NodeType]]
      case _ => traversal.filter { _.name == value }
    }

    /** Traverse to nodes where name matches one of the elements in `values` exactly.
      */
    def nameExact(values: String*): Iterator[NodeType] =
      if (values.length == 1) nameExact(values.head)
      else {
        val valueSet = values.toSet
        traversal.filter { item => valueSet.contains(item.name) }
      }

    /** Traverse to nodes where name does not match the regular expression `value`.
      */
    def nameNot(pattern: String): Iterator[NodeType] = {
      if (!flatgraph.misc.Regex.isRegex(pattern)) {
        traversal.filter { node => node.name != pattern }
      } else {
        val matcher = flatgraph.misc.Regex.multilineMatcher(pattern)
        traversal.filterNot { item => matcher.reset(item.name).matches }
      }
    }

    /** Traverse to nodes where name does not match any of the regular expressions in `values`.
      */
    def nameNot(patterns: String*): Iterator[NodeType] = {
      val matchers = patterns.map(flatgraph.misc.Regex.multilineMatcher)
      traversal.filter { item => matchers.find { _.reset(item.name).matches }.isEmpty }
    }

    /** Traverse to order property */
    def order: Iterator[Int] =
      traversal.map(_.order)

    /** Traverse to nodes where the order equals the given `value`
      */
    def order(value: Int): Iterator[NodeType] =
      traversal.filter { _.order == value }

    /** Traverse to nodes where the order equals at least one of the given `values`
      */
    def order(values: Int*): Iterator[NodeType] = {
      val vset = values.toSet
      traversal.filter { node => vset.contains(node.order) }
    }

    /** Traverse to nodes where the order is not equal to the given `value`
      */
    def orderNot(value: Int): Iterator[NodeType] =
      traversal.filter { _.order != value }

    /** Traverse to nodes where the order is not equal to any of the given `values`
      */
    def orderNot(values: Int*): Iterator[NodeType] = {
      val vset = values.toSet
      traversal.filter { node => !vset.contains(node.order) }
    }

    /** Traverse to nodes where the order is greater than the given `value`
      */
    def orderGt(value: Int): Iterator[NodeType] =
      traversal.filter { _.order > value }

    /** Traverse to nodes where the order is greater than or equal the given `value`
      */
    def orderGte(value: Int): Iterator[NodeType] =
      traversal.filter { _.order >= value }

    /** Traverse to nodes where the order is less than the given `value`
      */
    def orderLt(value: Int): Iterator[NodeType] =
      traversal.filter { _.order < value }

    /** Traverse to nodes where the order is less than or equal the given `value`
      */
    def orderLte(value: Int): Iterator[NodeType] =
      traversal.filter { _.order <= value }

    /** Traverse to stringList property */
    def stringList: Iterator[String] =
      traversal.flatMap(_.stringList)

  }
  /* accessors for base nodes end */
}
trait ConcreteStoredConversions extends ConcreteBaseConversions {
  import Accessors.*
  implicit def accessPropertyDescriptionTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]](
    traversal: IterableOnce[NodeType]
  ): Traversal_Property_description[NodeType] = new Traversal_Property_description(traversal.iterator)
  implicit def accessPropertyNameTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasNameEMT]](
    traversal: IterableOnce[NodeType]
  ): Traversal_Property_name[NodeType] = new Traversal_Property_name(traversal.iterator)
  implicit def accessPropertyOrderTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasOrderEMT]](
    traversal: IterableOnce[NodeType]
  ): Traversal_Property_order[NodeType] = new Traversal_Property_order(traversal.iterator)
  implicit def accessPropertyStringListTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]](
    traversal: IterableOnce[NodeType]
  ): Traversal_Property_string_list[NodeType] = new Traversal_Property_string_list(traversal.iterator)
}

trait ConcreteBaseConversions {
  import Accessors.*
  implicit def traversal_ThingBase[NodeType <: nodes.ThingBase](traversal: IterableOnce[NodeType]): Traversal_ThingBase[NodeType] =
    new Traversal_ThingBase(traversal.iterator)
}
