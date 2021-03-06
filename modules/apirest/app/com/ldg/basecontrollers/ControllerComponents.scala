/**
  * Created by ldipotet on 26/6/17.
  */

package com.ldg.basecontrollers

import play.api.i18n._
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc._
import play.api.Logger
import javax.inject._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.ldg.implicitconversions.ImplicitConversions._

import scala.util.Try
/**
  *
  * We import the default global execution context for execute tasks submitted to them.
  * It's essential for the Future.apply method. We encourage You to define your own execution contexts and use
  * them with Future, for now it enough import the default execution context as it is shown .
  *
  * It model a component needed for every controller instantiated in the project
  *
  */

sealed trait ControllerComponents {
  def defaultActionBuilder: ActionBuilder[Request]
  def jsonActionBuilder: ActionBuilder[Request]
  //def messagesApi: MessagesApi
  def langs: Langs
  //def executionContext: scala.concurrent.ExecutionContext
}

//TODO: Internationalization of messages
//TODO: ErrorHandler
//TODO: LogHandler

  /**
    * Action builder for simple Request.
    *
    * We only process request with content-type
    * Content-type specifications: https://tools.ietf.org/html/rfc7231#section-3.1.1.5
    * So here only is processed request
    *
    */

  class DefaultActionBuilder extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]):Future[Result] = {

      //request.contentType match {
      request.contentType.fold{Some("text/plain")}{content=>Some(content)} match {

        case Some("text/plain") => Logger.info("Simple HttpResquest")
          Logger.info("Correct Content-type in Header: text/plain")
          block(request) recover { case reqerror: Exception =>
            //we are dealing here with and unhandled exception
            Logger.error("Unexpected error  processing HttpRequest",reqerror)
            Results.InternalServerError
          }
        case Some(_) => Future.successful {
          val ownContentType:String = request.contentType.getOrElse("Unespecified Content-Type")
          Logger.error(s"WRONG Content-Type in Header, must be:text/plain and it is: $ownContentType")
          Results.Forbidden(Json.obj("status" ->"KO", "message" -> s"Content-Type is: $ownContentType and must be: text/plain "))
        }
          /*
        case None => Future.successful {
          Logger.error("Bad request, doesn't has Content-Type")
          //the client has sent a request without Content-Type so we build a Response Code 400
          Results.BadRequest(Json.obj("status" ->"KO", "message" -> "Bad request, doesn't has Content-Type"))
        }*/
      }
    }
  }

/**
  * Action builder for POST Request.
  *
  * Only treated POST request with application/json
  *
  * Fine grain error granularity in which:
  *
  * - we throw Forbidden if we have a content-type but it isn't an application/json .
  * - we throw BadRequest if the request doesn't has content-type
  *
  * Status-Code Definitions : http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
  *
  * Remember: user-id & password should be as entity header
  * https://stackoverflow.com/questions/36617948/play-2-4-actionbuilder-actionfunction-bodyparsers-and-json?rq=1
  */

class JsonActionBuilder extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {

    request.contentType match {

      case Some("application/json") => Logger.info("Simple HttpResquest")
        Logger.info("REQUEST: Json HttpRequest/ Json/Application")

        jsonBody(request.body) match {

          /**
            * we don't want to track the error in case of POST with a wrong json
            * and you want to consider only some specific Model add some many Cases as
            * model you neeed
            */

          case Some(js) if jsonValidator[com.ldg.model.Match](js).isSuccess => {
            //jsValue.as[com.ldg.model.Match] Validate de Json
            block(request) recover { case reqerror: Exception =>
              //we are dealing here with and unhandled exception
              Logger.error("Unexpected error  processing HttpRequest", reqerror)
              Results.InternalServerError
            }
          }

          case _ => Future.successful {
            //the client has sent a request without Content-Type so we build a Response Code 400
            Results.BadRequest(Json.obj("status" -> "KO", "message" -> "Malformed Json or Json Not Allowed"))
          }

        }

      case Some(_) => Future.successful {
        val ownContentType: String = request.contentType.getOrElse("Unespecified Content-Type")
        Logger.error(s"WRONG Content-Type in Header, must be: application/json and it is: $ownContentType")
        Results.Forbidden(Json.obj("status" -> "KO", "message" -> s"Content-Type is: $ownContentType and must be: application/json "))
      }
      case None => Future.successful {
        Logger.error("Bad request, doesn't has Content-Type")
        //the client has sent a request without Content-Type so we build a Response Code 400
        Results.BadRequest(Json.obj("status" -> "KO", "message" -> "Bad request, doesn't has Content-Type"))
      }
    }
  }

  def jsonValidator[A](optJsvalue:JsValue)(implicit redable: Reads[A]): Try[A] =  {
    Try(
      //can deal an IOException
      optJsvalue.as[A]
    )
  }

  def jsonBody[A](body: A)= body match {
    case jsvalue: JsValue => Some(jsvalue)
    case anycontent: AnyContent => anycontent.asJson
    case _ => None
  }

}

case class DefaultControllerComponents @Inject() (
                                                   defaultActionBuilder: DefaultActionBuilder,
                                                   jsonActionBuilder: JsonActionBuilder,
                                                   /*messagesApi: MessagesApi,*/
                                                   langs: Langs/*,
                                                   executionContext: scala.concurrent.ExecutionContext*/)
  extends ControllerComponents
