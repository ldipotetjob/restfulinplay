/**
  * Created by ldipotet on 28/6/17.
  */

package com.ldg.basecontrollers

import play.api.libs.json.{JsError, JsValue, Json, Reads}
import play.api.mvc._


import scala.concurrent.ExecutionContext.Implicits.global

/**
  * BaseController is the controller for all Actions in the project
  *
  * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
  *      a builder of actions that process POST request
  * @see utilities.DefaultControllerComponents.defaultActionBuilder a reference to DefaultActionBuilder
  *      a builder of actions that process GET/DELETE resquest
  *
  */
class BaseController(action: DefaultControllerComponents) extends Controller{

  def validateJson[A : Reads] = BodyParsers.parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def validateJsonTmp[A : Reads] = BodyParsers.parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * Process POST request
    *
    * In point of code where you use JsonAction must be create/imported the implicit conversion for
    * the expected type
    *
    * requestwithresult: Function that receives the request that will be processed with its result
    *
    */
  def JsonAction[A](requestwithresult: Request[JsValue] => Result)(implicit reads: Reads[A]) = {

    action.jsonActionBuilder(BodyParsers.parse.json) {  request =>

      val actionResult = request.body.validate[A]

      actionResult.fold(
        errors => {
          BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
        },
        jsonParsed => {
          //if everything goes well we simply let pass the request/result
          requestwithresult(request)
        }
      )
    }

  }


}
