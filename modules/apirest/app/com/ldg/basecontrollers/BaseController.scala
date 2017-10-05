/**
  * Created by ldipotet on 28/6/17.
  */

package com.ldg.basecontrollers

import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future.{successful => successfuture}


/**
  * BaseController is the controller for all Actions in the project
  *
  * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
  *      a builder of actions that process customized POST request.
  * @see JsonAction a builder of actions that process generic POST request.
  * @see utilities.DefaultControllerComponents.defaultActionBuilder a reference to DefaultActionBuilder
  *      a builder of actions that process GET/DELETE request.
  *
  */
class BaseController(action: DefaultControllerComponents) extends Controller{

  def validateJson[A : Reads] = BodyParsers.parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * There are several ways to implement action from builders
    * @see https://www.playframework.com/documentation/2.6.x/ScalaActionsComposition#custom-action-builders
    *
    * In this case we have use this because was necessary validate the Json POSTED versus an specific model
    * supplied by the client and at the same time the content-type whit our custom messages and our specific
    * validations
    *
    * This is a generic Action that let make a Customized Request Validation so it can be Used for any request
    * with a previous Reads Model implemented
    *
    * @param validator a reader that let you validate an specific json with its
    *                   corresponding model
    */

  def JsonAction[T](validator: Reads[T]) = new ActionBuilder[Request] {

    def jsonBody[A](body: A): Option[JsValue] = body match {
      case jsvalue: JsValue => Some(jsvalue)
      case anycontent: AnyContent => anycontent.asJson
      case _ => None
    }

    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      jsonBody(request.body) match  {
        case Some(js) => js.validate(validator) match {
          case JsSuccess(_, _) => block(request)
          case JsError(e) =>  successfuture(BadRequest(s"Bad Json: $e"))
        }
        case None =>  successfuture(UnsupportedMediaType("Bad content type: expected Json"))
      }
    }

  }

}