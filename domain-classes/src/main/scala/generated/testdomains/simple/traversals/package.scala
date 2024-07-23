package testdomains.simple

import testdomains.simple.nodes

package object traversals {

  /** not supposed to be used directly by users, hence the `bootstrap` in the name */
  object languagebootstrap extends ConcreteStoredConversions

  trait ConcreteStoredConversions extends ConcreteBaseConversions {
    implicit def accessPropertyDescriptionTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasDescriptionEMT]](
      traversal: IterableOnce[NodeType]
    ): TraversalPropertyDescription[NodeType] = new TraversalPropertyDescription(traversal.iterator)
    implicit def accessPropertyNameTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasNameEMT]](
      traversal: IterableOnce[NodeType]
    ): TraversalPropertyName[NodeType] = new TraversalPropertyName(traversal.iterator)
    implicit def accessPropertyOrderTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasOrderEMT]](
      traversal: IterableOnce[NodeType]
    ): TraversalPropertyOrder[NodeType] = new TraversalPropertyOrder(traversal.iterator)
    implicit def accessPropertyStringListTraversal[NodeType <: nodes.StoredNode & nodes.StaticType[nodes.HasStringListEMT]](
      traversal: IterableOnce[NodeType]
    ): TraversalPropertyStringList[NodeType] = new TraversalPropertyStringList(traversal.iterator)
  }

  trait ConcreteBaseConversions {
    implicit def traversalThingBase[NodeType <: nodes.ThingBase](traversal: IterableOnce[NodeType]): TraversalThingBase[NodeType] =
      new TraversalThingBase(traversal.iterator)
  }
}
