package testdomains.simple.traversals

import testdomains.simple.nodes
import testdomains.simple.accessors.languagebootstrap.*

final class TraversalPropertyStringList[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]](
  val traversal: Iterator[NodeType]
) extends AnyVal {

  /** Traverse to stringList property */
  def stringList: Iterator[String] =
    traversal.flatMap(_.stringList)

}
