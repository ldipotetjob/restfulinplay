package utilities

import play.api.i18n._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import javax.inject._
import play.api.Logger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ldipotet on 26/6/17.
  *
  * We import the default global execution context for execute tasks submitted to them.
  * It's essential for the Future.apply method. We encourage You to define your own execution contexts and use
  * them with Future, for now it is sufficient to know that you can import the default execution context as shown .
  *
  * It model a component needed for every controller instantiated in the project
  *
  */

sealed trait ControllerComponents {
  def defaultActionBuilder: ActionBuilder[Request]
  def jsonActionBuilder: ActionBuilder[Request]
  def messagesApi: MessagesApi
  def langs: Langs
  def executionContext: scala.concurrent.ExecutionContext
}

//TODO: a function that process every content-type for be called from every actionBuilder (DefaultActionBuilder,JsonActionBuilder)
//TODO: internationalization of message

  /**
    * Action builder for simple Request.
    *
    * We only process request with content-type
    * Content-type specifications: https://tools.ietf.org/html/rfc7231#section-3.1.1.5
    *
    */

  class DefaultActionBuilder extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      request.contentType match {

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
        case None => Future.successful {
          //the client has sent a request without Content-Type so we build a Response Code 400
          Results.BadRequest(Json.obj("status" ->"KO", "message" -> "Bad request, doesn't has Content-Type"))
        }
      }
    }
  }

/*

For dealing with authentication request !!

class Authenticated extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    if(request.headers.get("Authorization").isDefined)
      block(request)
    else
      Future.successful{Results.Status(Status.UNAUTHORIZED)}
  }
}

*/

/**
  * Action builder for POST Request.
  *
  * Only treated POST request with application/json
  *
  * Fine grain error granularity in which:
  *
  * - we throw ---- if we have a content-type but it isn't an application/json .
  * - we throw BadRequest if the request doesn't has content-type
  *
  * Status-Code Definitions : http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
  *
  * Remember: user-id & password should be as entity header
  *
  */

class JsonActionBuilder extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {

    request.contentType match {

      case Some("application/json") => Logger.info("Simple HttpResquest")
        Logger.info("REQUEST: Json HttpRequest/ Json/Application")
        block(request) recover { case reqerror: Exception =>
          //we are dealing here with and unhandled exception
          Logger.error("Unexpected error  processing HttpRequest",reqerror)
          Results.InternalServerError
        }
      case Some(_) => Future.successful {
        val ownContentType:String = request.contentType.getOrElse("Unespecified Content-Type")
        Results.Forbidden(Json.obj("status" ->"KO", "message" -> s"Content-Type is: $ownContentType and must be: application/json "))
      }
      case None => Future.successful {
        //the client has sent a request without Content-Type so we build a Response Code 400
        Results.BadRequest(Json.obj("status" ->"KO", "message" -> "Bad request, doesn't has Content-Type"))
      }
    }
  }
}


case class DefaultControllerComponents @Inject() (
                                                   defaultActionBuilder: DefaultActionBuilder,
                                                   jsonActionBuilder: JsonActionBuilder,
                                                   messagesApi: MessagesApi,
                                                   langs: Langs,
                                                   executionContext: scala.concurrent.ExecutionContext)
  extends ControllerComponents
