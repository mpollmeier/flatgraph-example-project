package testdomains.simple.traversals

import testdomains.simple.nodes
import testdomains.simple.accessors.languagebootstrap.*

final class TraversalPropertyDescription[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]](
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
