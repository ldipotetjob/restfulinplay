/**
  * Created by ldipotet on 28/6/17.
  */

package com.ldg.basecontrollers

import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import scala.reflect.ClassTag

//TODO: Internationalization of messages
//TODO: ErrorHandler
//TODO: LogHandler

//The Accept request HTTP header advertises which content types, expressed as MIME types, the client is able
// to understand. Using content negotiation, the server then selects one of the proposals, uses it and informs the
// client of its choice with the Content-Type response header.

// ref.: http://jsonapi.org/format/#content-negotiation-clients

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
  def proccessContentNegotiation[C<:com.ldg.model.Parseable](content: Seq[C])(implicit request: Request[AnyContent],tag: ClassTag[C], writeable: Writes[C]): Future[Result] = {
     val AcceptsCSV = Accepting("text/csv")
    render.async {
      case Accepts.Json() => Future.successful(Ok(Json.obj("status" ->"OK", "message" -> Json.toJson(content) )).withHeaders(CONTENT_TYPE->JSON))
      case AcceptsCSV() => Future.successful(Ok( selecTemplate[C](content)).withHeaders(CONTENT_TYPE->"text/csv"))
      case _ => Future.successful(NotAcceptable("Accept value can not be served, only serve text/csv or application/json"))
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
      case _ => NotAcceptable("Accept value can not be served, only serve txt/csv or application/json")
    }
  }

  /**
    *  Select the right csv template that must be returned with the response
    */

  def selecTemplate[T](genericSeq:Seq[Any])(implicit tag: ClassTag[T]) ={

    extractRightCollection[T](genericSeq) match {

      /**
        * This match can NOT be generic. For every kind of model we need an specific template.
        * So you should have so many "Templates" and so many "Case" as report as you need.
        * Now we have only views.csv.football In real case you can have as many template as you need.
        *
        */

      case Some(matchgame:com.ldg.model.Match) => {

        val matchseq:Seq[com.ldg.model.Match] = genericSeq.asInstanceOf[Seq[com.ldg.model.Match]]

        views.csv.football(matchseq)

      }
      case _ => views.csv.football(Seq[com.ldg.model.Match]())
    }
  }

  /**
    * Extractor method that let to it's caller  check the type of collection
    *
    * @param list sequence of T element
    * @param tag for preventing type erasure
    * @tparam T kind of element
    * @return
    */
  def extractRightCollection[T](list: Seq[Any])(implicit tag: ClassTag[T]): Option[T] =
    list.headOption match {

      case Some(element:T) => Some(element)

      case _ => Option.empty[T]
    }

    /**
      * TODO warning!!
      *
      * [info] application - Correct Content-type in Header: text/plain
      * [warn] p.c.s.n.NettyModelConversion - Content-Type set both in header (text/csv) and
      * attached to entity (text/csv),ignoring content type from entity.
      * To remove this warning, use Result.as(...) to set the content type, rather than
      * setting the header manually.
      *
      */
}