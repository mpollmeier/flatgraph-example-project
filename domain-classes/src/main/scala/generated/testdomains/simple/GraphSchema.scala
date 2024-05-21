package testdomains.simple

import testdomains.simple.nodes
import testdomains.simple.edges
import flatgraph.FormalQtyType

object GraphSchema extends flatgraph.Schema {
  private val nodeLabels                             = IndexedSeq("thing")
  val nodeKindByLabel                                = nodeLabels.zipWithIndex.toMap
  val edgeLabels                                     = Array("connected_to")
  val edgeKindByLabel                                = edgeLabels.zipWithIndex.toMap
  val edgePropertyAllocators: Array[Int => Array[?]] = Array(size => Array.fill(size)("<empty>") /* label = connected_to, id = 0 */ )
  val nodeFactories: Array[(flatgraph.Graph, Int) => nodes.StoredNode] = Array((g, seq) => new nodes.Thing(g, seq))
  val edgeFactories: Array[(flatgraph.GNode, flatgraph.GNode, Int, Any) => flatgraph.Edge] =
    Array((s, d, subseq, p) => new edges.ConnectedTo(s, d, subseq, p))
  val nodePropertyAllocators: Array[Int => Array[?]] =
    Array(size => new Array[String](size), size => new Array[String](size), size => new Array[Int](size), size => new Array[String](size))
  val normalNodePropertyNames = Array("description", "name", "order", "string_list")
  val nodePropertyByLabel     = normalNodePropertyNames.zipWithIndex.toMap
  val nodePropertyDescriptors: Array[FormalQtyType.FormalQuantity | FormalQtyType.FormalType] = {
    val nodePropertyDescriptors = new Array[FormalQtyType.FormalQuantity | FormalQtyType.FormalType](8)
    for (idx <- Range(0, 8)) {
      nodePropertyDescriptors(idx) =
        if ((idx & 1) == 0) FormalQtyType.NothingType
        else FormalQtyType.QtyNone
    }

    nodePropertyDescriptors(0) = FormalQtyType.StringType // thing.description
    nodePropertyDescriptors(1) = FormalQtyType.QtyOption
    nodePropertyDescriptors(2) = FormalQtyType.StringType // thing.name
    nodePropertyDescriptors(3) = FormalQtyType.QtyOne
    nodePropertyDescriptors(4) = FormalQtyType.IntType // thing.order
    nodePropertyDescriptors(5) = FormalQtyType.QtyOne
    nodePropertyDescriptors(6) = FormalQtyType.StringType // thing.string_list
    nodePropertyDescriptors(7) = FormalQtyType.QtyMulti
    nodePropertyDescriptors
  }
  override def getNumberOfNodeKinds: Int                          = 1
  override def getNumberOfEdgeKinds: Int                          = 1
  override def getNodeLabel(nodeKind: Int): String                = nodeLabels(nodeKind)
  override def getNodeKindByLabel(label: String): Int             = nodeKindByLabel.getOrElse(label, flatgraph.Schema.UndefinedKind)
  override def getEdgeLabel(nodeKind: Int, edgeKind: Int): String = edgeLabels(edgeKind)
  override def getEdgeKindByLabel(label: String): Int             = edgeKindByLabel.getOrElse(label, flatgraph.Schema.UndefinedKind)
  override def getNodePropertyNames(nodeLabel: String): Set[String] = {
    nodeLabel match {
      case "thing" => Set("description", "name", "order", "string_list")
      case _       => Set.empty
    }
  }
  override def getEdgePropertyName(label: String): Option[String] = {
    label match {
      case "connected_to" => Some("name")
      case _              => None
    }
  }

  override def getPropertyLabel(nodeKind: Int, propertyKind: Int): String = {
    if (propertyKind < 4) normalNodePropertyNames(propertyKind)
    else null
  }

  override def getPropertyKindByName(label: String): Int = nodePropertyByLabel.getOrElse(label, flatgraph.Schema.UndefinedKind)
  override def getNumberOfPropertyKinds: Int             = 4
  override def makeNode(graph: flatgraph.Graph, nodeKind: Short, seq: Int): nodes.StoredNode = nodeFactories(nodeKind)(graph, seq)
  override def makeEdge(src: flatgraph.GNode, dst: flatgraph.GNode, edgeKind: Short, subSeq: Int, property: Any): flatgraph.Edge =
    edgeFactories(edgeKind)(src, dst, subSeq, property)
  override def allocateEdgeProperty(nodeKind: Int, direction: flatgraph.Edge.Direction, edgeKind: Int, size: Int): Array[?] =
    edgePropertyAllocators(edgeKind)(size)
  override def getNodePropertyFormalType(nodeKind: Int, propertyKind: Int): FormalQtyType.FormalType = nodePropertyDescriptors(
    propertyOffsetArrayIndex(nodeKind, propertyKind)
  ).asInstanceOf[FormalQtyType.FormalType]
  override def getNodePropertyFormalQuantity(nodeKind: Int, propertyKind: Int): FormalQtyType.FormalQuantity = nodePropertyDescriptors(
    1 + propertyOffsetArrayIndex(nodeKind, propertyKind)
  ).asInstanceOf[FormalQtyType.FormalQuantity]
}
