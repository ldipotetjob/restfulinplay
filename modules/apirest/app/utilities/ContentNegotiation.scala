package utilities


import play.api.http._
import play.api.libs.json.{Writes, Json}
import play.api.mvc._
import com.ldg.model._

import scala.reflect.ClassTag


trait ContentNegotiation {



  //Self type annotation -> trait must be mixed into Controller (thus, has access to render, Accepts and Ok)
  self : Controller =>

  //Serializes the provided value according to the Accept headers of the (implicitly) provided request
  def proccessContentNegotiation[C<:com.ldg.model.Match](content: Seq[C])(implicit request: Request[AnyContent], writeable: Writes[C]) : Result = {
     val AcceptsCSV = Accepting("txt/csv")
    render {
      case Accepts.Json() => Ok(Json.obj("status" ->"OK", "message" -> Json.toJson(content) )).withHeaders(CONTENT_TYPE->JSON)
      //case AcceptsCSV => Ok( views.csv.premier(content)).withHeaders(CONTENT_TYPE->"text/csv")
      case AcceptsCSV() => Ok( selecTemplate(content)).withHeaders(CONTENT_TYPE->"text/csv")
      case _ => NotAcceptable("The aceptable value can not be served, only serve text/cvs or application/json")
    }
  }


  def selecTemplate(genericSeq:Seq[Any]) ={

    extractRightCollection[Match](genericSeq) match {

      case Some(matchgame:com.ldg.model.Match) => {

        val matchseq:Seq[com.ldg.model.Match] = genericSeq.asInstanceOf[Seq[com.ldg.model.Match]]
        views.csv.premier(matchseq)

      }
    }

  }

  def extractRightCollection[T](list: Seq[Any])(implicit tag: ClassTag[T]): Option[T] =
    list.head match {
      case element: T => Some(element)
      case _ => Option.empty[T]
    }

}