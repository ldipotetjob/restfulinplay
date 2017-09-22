/**
  * Created by ldipotet on 28/6/17.
  */

package com.ldg.basecontrollers

import play.api.libs.json.{Json, Writes}
import play.api.mvc._

import scala.reflect.ClassTag


trait ContentNegotiation {

  //Self type annotation -> trait must be mixed into Controller (thus, has access to render, Accepts and Ok)
  self : Controller =>

  //Serializes the provided value according to the Accept headers of the (implicitly) provided request
  def proccessContentNegotiation[C<:com.ldg.model.Parseable](content: Seq[C])(implicit request: Request[AnyContent],tag: ClassTag[C], writeable: Writes[C]) : Result = {
     val AcceptsCSV = Accepting("txt/csv")
    render {
      case Accepts.Json() => Ok(Json.obj("status" ->"OK", "message" -> Json.toJson(content) )).withHeaders(CONTENT_TYPE->JSON)
      case AcceptsCSV() => Ok( selecTemplate[C](content)).withHeaders(CONTENT_TYPE->"text/csv")
      case _ => NotAcceptable("The aceptable value can not be served, only serve text/cvs or application/json")
    }
  }

  def selecTemplate[T](genericSeq:Seq[Any])(implicit tag: ClassTag[T]) ={

    extractRightCollection[T](genericSeq) match {

      case Some(matchgame:com.ldg.model.Match) => {

        val matchseq:Seq[com.ldg.model.Match] = genericSeq.asInstanceOf[Seq[com.ldg.model.Match]]

        views.csv.premier(matchseq)

      }
      case _ => views.csv.premier(Seq[com.ldg.model.Match]())
    }
  }

  def extractRightCollection[T](list: Seq[Any])(implicit tag: ClassTag[T]): Option[T] =
    list.headOption match {

      case Some(element:T) => Some(element)

      case _ => Option.empty[T]
    }

  def fNone[T]: Option[T] = None



  //TODO Solve the following warning
    /*
    [info] application - Correct Content-type in Header: text/plain
    [warn] p.c.s.n.NettyModelConversion - Content-Type set both in header (text/csv) and attached to entity (text/csv), ignoring content type from entity. To remove this warning, use Result.as(...) to set the content type, rather than setting the header manually.
    */

}