/**
  * Created by ldipotet on 28/6/17.
  */

package com.ldg.basecontrollers

import play.api.libs.json._
import play.api.mvc._

import scala.reflect.ClassTag


trait ContentNegotiation {

  /**
    *
    * Self type annotation -> trait can be mixed into Controller without extends it
    *                         (thus, has access to render, Accepts and Ok and the rest of members
    *                         trait Controller)
    *
   */

  self : Controller =>

  //Serializes the provided value according to the Accept headers of the (implicitly) provided request
  def proccessContentNegotiation[C<:com.ldg.model.Parseable](content: Seq[C])(implicit request: Request[AnyContent],tag: ClassTag[C], writeable: Writes[C]): Result = {
     val AcceptsCSV = Accepting("text/csv")
    render {
      case Accepts.Json() => Ok(Json.obj("status" ->"OK", "message" -> Json.toJson(content) )).withHeaders(CONTENT_TYPE->JSON)
      case AcceptsCSV() => Ok( selecTemplate[C](content)).withHeaders(CONTENT_TYPE->"text/csv")
      case _ => NotAcceptable("The Accept value can not be served, only serve text/csv or application/json")
    }
  }



  /**
    *
    * Serializes the provided value according to the Accept headers of the (implicitly) provided request
    *
    * @param content
    * @param request Remember that its depend of the request Body and REMEMBER tha the body is transformed in case
    *                of any validator
    * @param tag preventing type erasure
    * @param writeable
    * @tparam C
    * @return
    */

  def processContentNegotiationForJson[C<:com.ldg.model.Parseable](content: C)()(implicit request: Request[AnyContent], tag: ClassTag[C], writeable: Writes[C]) : Result = {
    val AcceptTextPlain = Accepting("text/plain")
    render {
      case Accepts.Json() => Ok(Json.obj("status" ->"OK", "message" -> Json.toJson(content) )).withHeaders(CONTENT_TYPE->JSON)
      case AcceptTextPlain() => Ok("Process Done").withHeaders(CONTENT_TYPE->"text/plain")
      case _ => NotAcceptable("The Accept value can not be served, only serve txt/csv or application/json")
    }
  }


  def selecTemplate[T](genericSeq:Seq[Any])(implicit tag: ClassTag[T]) ={

    extractRightCollection[T](genericSeq) match {

      /**
        * This match can NOT be generic. For every kind of model we need an specific template.
        * So you should have so many "Templates" and so many "Case" as report you need.
        * Now we have only views.csv.premier In real case you can have as many template as you need.
        *
        */

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

  //TODO Solve the following warning
    /*
    [info] application - Correct Content-type in Header: text/plain
    [warn] p.c.s.n.NettyModelConversion - Content-Type set both in header (text/csv) and attached to entity (text/csv), ignoring content type from entity. To remove this warning, use Result.as(...) to set the content type, rather than setting the header manually.
    */

}