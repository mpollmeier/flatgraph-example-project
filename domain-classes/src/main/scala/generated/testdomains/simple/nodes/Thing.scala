package testdomains.simple.nodes

import testdomains.simple.language.*
import scala.collection.immutable.{IndexedSeq, ArraySeq}
import scala.collection.mutable

/** Node base type for compiletime-only checks to improve type safety. EMT stands for: "erased marker trait", i.e. it is erased at runtime
  */
trait ThingEMT extends AnyRef with HasDescriptionEMT with HasNameEMT with HasOrderEMT with HasStringListEMT

trait ThingBase extends AbstractNode with StaticType[ThingEMT] {

  override def propertiesMap: java.util.Map[String, Any] = {
    import testdomains.simple.accessors.languagebootstrap.*
    val res = new java.util.HashMap[String, Any]()
    this.description.foreach { p => res.put("description", p) }
    if (("<empty>": String) != this.name) res.put("name", this.name)
    if ((1: Int) != this.order) res.put("order", this.order)
    val tmpStringList = this.stringList; if (tmpStringList.nonEmpty) res.put("string_list", tmpStringList)
    res
  }
}

object Thing {
  val Label = "thing"
  object PropertyNames {

    val Description = "description"

    val Name = "name"

    val Order = "order"

    val StringList = "string_list"
  }
  object Properties {
    val Description = flatgraph.OptionalPropertyKey[String](kind = 0, name = "description")
    val Name        = flatgraph.SinglePropertyKey[String](kind = 1, name = "name", default = "<empty>")
    val Order       = flatgraph.SinglePropertyKey[Int](kind = 2, name = "order", default = 1: Int)
    val StringList  = flatgraph.MultiPropertyKey[String](kind = 3, name = "string_list")
  }
  object PropertyDefaults {
    val Name  = "<empty>"
    val Order = 1: Int
  }
}

class Thing(graph_4762: flatgraph.Graph, seq_4762: Int)
    extends StoredNode(graph_4762, 0.toShort, seq_4762)
    with ThingBase
    with StaticType[ThingEMT] {

  override def productElementName(n: Int): String =
    n match {
      case 0 => "description"
      case 1 => "name"
      case 2 => "order"
      case 3 => "stringList"
      case _ => ""
    }

  override def productElement(n: Int): Any =
    n match {
      case 0 => this.description
      case 1 => this.name
      case 2 => this.order
      case 3 => this.stringList
      case _ => null
    }

  override def productPrefix = "Thing"
  override def productArity  = 4

  override def canEqual(that: Any): Boolean = that != null && that.isInstanceOf[Thing]
}

object NewThing {
  def apply(): NewThing                              = new NewThing
  private val outNeighbors: Map[String, Set[String]] = Map("connected_to" -> Set("thing"))
  private val inNeighbors: Map[String, Set[String]]  = Map("connected_to" -> Set("thing"))

  object InsertionHelpers {
    object NewNodeInserter_Thing_description extends flatgraph.NewNodePropertyInsertionHelper {
      override def insertNewNodeProperties(newNodes: mutable.ArrayBuffer[flatgraph.DNode], dst: AnyRef, offsets: Array[Int]): Unit = {
        if (newNodes.isEmpty) return
        val dstCast = dst.asInstanceOf[Array[String]]
        val seq     = newNodes.head.storedRef.get.seq()
        var offset  = offsets(seq)
        var idx     = 0
        while (idx < newNodes.length) {
          val nn = newNodes(idx)
          nn match {
            case generated: NewThing =>
              generated.description match {
                case Some(item) =>
                  dstCast(offset) = item
                  offset += 1
                case _ =>
              }
            case _ =>
          }
          assert(seq + idx == nn.storedRef.get.seq(), "internal consistency check")
          idx += 1
          offsets(idx + seq) = offset
        }
      }
    }
    object NewNodeInserter_Thing_name extends flatgraph.NewNodePropertyInsertionHelper {
      override def insertNewNodeProperties(newNodes: mutable.ArrayBuffer[flatgraph.DNode], dst: AnyRef, offsets: Array[Int]): Unit = {
        if (newNodes.isEmpty) return
        val dstCast = dst.asInstanceOf[Array[String]]
        val seq     = newNodes.head.storedRef.get.seq()
        var offset  = offsets(seq)
        var idx     = 0
        while (idx < newNodes.length) {
          val nn = newNodes(idx)
          nn match {
            case generated: NewThing =>
              dstCast(offset) = generated.name
              offset += 1
            case _ =>
          }
          assert(seq + idx == nn.storedRef.get.seq(), "internal consistency check")
          idx += 1
          offsets(idx + seq) = offset
        }
      }
    }
    object NewNodeInserter_Thing_order extends flatgraph.NewNodePropertyInsertionHelper {
      override def insertNewNodeProperties(newNodes: mutable.ArrayBuffer[flatgraph.DNode], dst: AnyRef, offsets: Array[Int]): Unit = {
        if (newNodes.isEmpty) return
        val dstCast = dst.asInstanceOf[Array[Int]]
        val seq     = newNodes.head.storedRef.get.seq()
        var offset  = offsets(seq)
        var idx     = 0
        while (idx < newNodes.length) {
          val nn = newNodes(idx)
          nn match {
            case generated: NewThing =>
              dstCast(offset) = generated.order
              offset += 1
            case _ =>
          }
          assert(seq + idx == nn.storedRef.get.seq(), "internal consistency check")
          idx += 1
          offsets(idx + seq) = offset
        }
      }
    }
    object NewNodeInserter_Thing_stringList extends flatgraph.NewNodePropertyInsertionHelper {
      override def insertNewNodeProperties(newNodes: mutable.ArrayBuffer[flatgraph.DNode], dst: AnyRef, offsets: Array[Int]): Unit = {
        if (newNodes.isEmpty) return
        val dstCast = dst.asInstanceOf[Array[String]]
        val seq     = newNodes.head.storedRef.get.seq()
        var offset  = offsets(seq)
        var idx     = 0
        while (idx < newNodes.length) {
          val nn = newNodes(idx)
          nn match {
            case generated: NewThing =>
              for (item <- generated.stringList) {
                dstCast(offset) = item
                offset += 1
              }
            case _ =>
          }
          assert(seq + idx == nn.storedRef.get.seq(), "internal consistency check")
          idx += 1
          offsets(idx + seq) = offset
        }
      }
    }
  }
}

class NewThing extends NewNode(0.toShort) with ThingBase {
  override type StoredNodeType = Thing
  override def label: String = "thing"

  override def isValidOutNeighbor(edgeLabel: String, n: NewNode): Boolean = {
    NewThing.outNeighbors.getOrElse(edgeLabel, Set.empty).contains(n.label)
  }
  override def isValidInNeighbor(edgeLabel: String, n: NewNode): Boolean = {
    NewThing.inNeighbors.getOrElse(edgeLabel, Set.empty).contains(n.label)
  }

  var description: Option[String]                        = None
  var name: String                                       = "<empty>": String
  var order: Int                                         = 1: Int
  var stringList: IndexedSeq[String]                     = ArraySeq.empty
  def description(value: Option[String]): this.type      = { this.description = value; this }
  def description(value: String): this.type              = { this.description = Option(value); this }
  def name(value: String): this.type                     = { this.name = value; this }
  def order(value: Int): this.type                       = { this.order = value; this }
  def stringList(value: IterableOnce[String]): this.type = { this.stringList = value.iterator.to(ArraySeq); this }
  override def countAndVisitProperties(interface: flatgraph.BatchedUpdateInterface): Unit = {
    interface.countProperty(this, 0, description.size)
    interface.countProperty(this, 1, 1)
    interface.countProperty(this, 2, 1)
    interface.countProperty(this, 3, stringList.size)
  }

  override def copy: this.type = {
    val newInstance = new NewThing
    newInstance.description = this.description
    newInstance.name = this.name
    newInstance.order = this.order
    newInstance.stringList = this.stringList
    newInstance.asInstanceOf[this.type]
  }

  override def productElementName(n: Int): String =
    n match {
      case 0 => "description"
      case 1 => "name"
      case 2 => "order"
      case 3 => "stringList"
      case _ => ""
    }

  override def productElement(n: Int): Any =
    n match {
      case 0 => this.description
      case 1 => this.name
      case 2 => this.order
      case 3 => this.stringList
      case _ => null
    }

  override def productPrefix                = "NewThing"
  override def productArity                 = 4
  override def canEqual(that: Any): Boolean = that != null && that.isInstanceOf[NewThing]
}
