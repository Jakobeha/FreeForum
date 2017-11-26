package myutils

import play.twirl.api.Html

import scala.util.{Failure, Success, Try}
import scala.xml._

object HtmlUtils {
  private implicit class NodeMapExt(private val self: Node) {
    /** Whether this node contains no children. */
    def isLeaf: Boolean = children.isEmpty
    /** Whether this node contains children. */
    def isTree: Boolean = children.nonEmpty

    /** This node's children -- @child@ excluding itself. */
    def children: Seq[Node] = self.child.tail
    def overChildren(transformer: (Node) => Node): Node = {
      if (isLeaf) { // Is a leaf
        self
      } else { // Is a tree
        Elem(
          self.prefix,
          self.label,
          self.attributes,
          self.scope,
          minimizeEmpty = false,
          children.map(transformer): _*
        )
      }
    }
  }

  private val safeTags: Seq[SafeTag] = Seq(
    SafeTag(name = "b", attrs = Set.empty),
    SafeTag(name = "i", attrs = Set.empty),
    SafeTag(name = "u", attrs = Set.empty),
    SafeTag(name = "code", attrs = Set.empty),
    SafeTag(name = "pre", attrs = Set.empty),
    SafeTag(name = "a", attrs = Set("href")),
    SafeTag(name = "code", attrs = Set.empty)
  )
  /**
    * Contains safe tag names as keys, and their safe properties as values.
    */
  private val safeTagsMap: Map[String, Set[String]] = safeTags.map { tag => (tag.name, tag.attrs) }.toMap

  /**
    * Allows certain safe nodes (like bold, italics, underline),
    * but escapes everything else (converts it into literal text).
    */
  def escapeUnsafe(rawHtml: String): Html = {
    val tryNodes = Try(XML.loadString(s"<xml>$rawHtml</xml>").child)
    tryNodes match {
      case Success(nodes) => Html(nodes.map(escapeUnsafe).mkString)
      case Failure(_) => Html(escape(rawHtml).toString) // Escapes everything
    }
  }

  /**
    * Allows certain safe nodes (like bold, italics, underline),
    * but escapes everything else (converts it into literal text).
    */
  def escapeUnsafe(node: Node): Node = {
    if (node.isAtom) {
      node
    } else {
      def shallowEscape(node: Node): Node = {
        val safeNode = for {
          safeAttrs <- safeTagsMap.get(node.label)
          if node.attributes.asAttrMap.keySet.subsetOf(safeAttrs)
        } yield node
        safeNode.getOrElse(escape(node))
      }

      shallowEscape(node).overChildren(escapeUnsafe)
    }
  }

  def escape(rawHtml: String): Node = {
    <span class="unsafe-html">{ rawHtml }</span> // Scala escapes the HTML itself
  }

  def escape(node: Node): Node = escape(node.toString)
}

case class SafeTag(name: String, attrs: Set[String])