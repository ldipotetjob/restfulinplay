package controllers

import javax.inject._

import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc.AnyContent
import utilities.MyOwnConversions._
import utilities.ImplicitConvertions._
import utilities.{BaseController, DefaultControllerComponents, Place}
import com.ldg.model._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PremierleagueController @Inject()(action: DefaultControllerComponents) extends BaseController(action) {

  // some curl templates for testing POST request:
  /*
     for JsonAction[Match]:
     curl -d '{"name":"Nuthanger Farm","location":{"lat" : 51.244031,"long" : -1.263224},"residents" : [{"name" : "Fiver","age" : 4,"role" : null},{"name" : "Bigwig","age" : 6, "role" : "Owsla"}]}' -H "Content-Type: application/json" http://localhost:9000/apirest/newsave ; echo

     for
  */
  /**
    * Best way, using a wrapper for a action that process a POST with a JSON in the request.
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    *
    * @return specific Result when every things is Ok so we send the status and the comment with the specific
    *         json that show the result
    */

  def insertMatch = JsonAction[Match] { request =>

   val jsonMatchObject= request.body.as[Match]
    //as the specifications https://www.playframework.com/documentation/2.6.x/ScalaResults
    //The result content type is automatically inferred from the Scala value that you specify as the response body.
    Ok(Json.obj("status" ->"OK", "message" -> (jsonMatchObject) ))
  }

  /**
    * The simplest way for process a request, in this case we call a default action builder
    * and process inside the action converting a body in a json, if something goes wrong we have
    * an NONE and therefore a BadRequest in other case a OK (200) of Response is get back.
    *
    * Action NOT registered in the route file. It's just for show how modify in
    * some responses the HTTP header. If you want to test it you will need to add it in
    * the route file.
    *
    * @return Result (Ok/BadRequest)
    */

  def save = action.defaultActionBuilder { request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    // Expecting json body
    jsonBody.map { json =>
      Ok("Got: " + (json \ "name").as[String])
    }.getOrElse {
      BadRequest("Expecting application/json request body")
    }
  }

  /**
    * A way for process a Json request, using a base action, in this case we call a json action builder
    * and use the specific BodyParser validator for the JSON, if something goes wrong the
    * same Body parser Validator throw the BadRequest in other case an OK (200) of Response is get back
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    *
    * @return Result (Ok/BadRequest)
    */

  def savePlaceConcise = action.jsonActionBuilder(validateJson[Place]) { request =>
    // `request.body` contains a fully validated `Place` instance.
    val place = request.body

    Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
  }
}