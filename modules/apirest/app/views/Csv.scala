/**
  * Created by ldipotet on 23/7/17.
  */


package views

/**
  *
  * Our Template is NOT include in the common template formats
  * so we need to add support for our own format.
  *
  */
import play.api.http.ContentTypeOf
import play.api.mvc.Codec
import play.twirl.api._

import scala.collection.immutable
import scala.collection.immutable.Seq


class Csv(buffer: immutable.Seq[Csv],text:String,escape:Boolean) extends BufferedContent[Csv](buffer, text) {

  val contentType = Csv.contentType

  def this(text: String) = this(Nil, Formats.safe(text),false)
  def this(buffer: immutable.Seq[Csv]) = this(buffer, "",false)


  override protected def buildString(builder: StringBuilder) {
    if (elements.nonEmpty) {
      elements.foreach { e =>
        e.buildString(builder)
      }
    } else if (escape) {
      // Using our own algorithm here because commons lang escaping wasn't designed for protecting against XSS, and there
      // don't seem to be any other good generic escaping tools out there.
      text.foreach {
        case '"' => builder.append("\"\"")
        case c => builder += c
      }
    } else {
      builder.append(text)
    }
  }
}


object Csv {
  val contentType = "text/csv"
  implicit def contentTypeCsv(implicit codec: Codec): ContentTypeOf[Csv] = ContentTypeOf[Csv](Some(Csv.contentType))

  def apply(text: String): Csv = new Csv(text)

  def empty: Csv = new Csv("")
}
object CsvFormat extends Format[Csv] {
  def raw(text: String): Csv = Csv(text)
  def escape(text: String): Csv = {
    new Csv(Nil, text, true)
  }

  def empty: Csv = new Csv("")

  def fill(elements: Seq[Csv]): Csv = new Csv(elements)
}