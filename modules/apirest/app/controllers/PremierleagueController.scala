/**
  * Created by ldipotet on 13/7/17.
  */

package controllers

import javax.inject._

import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import com.ldg.basecontrollers.{BaseController, ContentNegotiation, DefaultControllerComponents}
import com.ldg.implicitconversions.Place
import com.ldg.implicitconversions.MyOwnConversions._
import com.ldg.implicitconversions.ImplicitConvertions._
import com.ldg.model._
import services.TDataServices

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PremierleagueController @Inject()(action: DefaultControllerComponents,services:TDataServices) extends BaseController(action) with ContentNegotiation {


  // some curl templates for testing POST request:
  /*

    curl -d '{"date" : "1495502158120","season" : "MW 26","homeTeam" :{"name" : "Aston Villa","goals" : 3,"goalsPlayer" : {"Dean Saunders": ["44''", "66''"],"Garry Parker": ["78''"]}},"awayTeam" : {"name" : "Liverpool","goals" : 3,"goalsPlayer" : {"Mark Walters": ["43''"],"Ronnie Rosenthal": ["84''"],"John Williams": ["90''"]}}}'  -H "Content-Type: application/json" http://localhost:9000/apirest/premier/match ; echo

  */

  // other solution more elegant is put the json in a file and type de following command in your terminal:
  // curl -d "@myJasonFile.json"  -H "Content-Type: application/json" http://localhost:9000/apirest/premier/match ; echo

  /**
    * Best way, using a wrapper for a action that process a POST with a JSON in the request.
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    * @return specific Result when every things is Ok so we send the status and the comment with the specific
    *         json that show the result
    */

  def insertMatch = JsonAction[Match] { implicit request =>

   val jsonMatchObject= request.body.as[Match]
    //as the specifications https://www.playframework.com/documentation/2.6.x/ScalaResults
    //The result content type is automatically inferred from the Scala value that you specify as the response body.
    Ok(Json.obj("status" ->"OK", "message" -> jsonMatchObject ))
  }

  /**
    * curl template for testing GET request:
    *
    * curl -H "Content-Type: text/plain" http://localhost:9000/apirest/premier/matchs ; echo
    *
    * @return
    */

  def getMatchGame = action.defaultActionBuilder { implicit request =>

    def dataResults:Seq[Match] = services.modelOfMatchPremier("football.txt")

    proccessContentNegotiation[Match](dataResults)

  }

  /**
    * The simplest way for process a request, in this case we call a default action builder
    * and process inside the action converting a body in a json, if something goes wrong we have
    * an NONE and therefore a BadRequest in other case a OK (200) of Response is get back.
    *
    * Action NOT registered in the route file. It's just for show how modify the HTTP header
    * some responses . If you want to test it you will need to add it in
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
    *
    * for JsonAction[Place]:
    * curl -d '{"name":"Nuthanger Farm","location":{"lat" : 51.244031,"long" : -1.263224},"residents" : [{"name" : "Fiver","age" : 4,"role" : null},{"name" : "Bigwig","age" : 6, "role" : "Owsla"}]}' -H "Content-Type: application/json" http://localhost:9000/apirest/newsave ; echo
    *
    * A way for process a Json request, using a base action, in this case we call a json action builder
    * and use the specific BodyParser validator for the JSON, if something goes wrong the
    * same Body parser Validator throw the BadRequest in other case an OK (200) of Response is get back
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    * @return Result (Ok/BadRequest)
    */

  def savePlaceConcise = action.jsonActionBuilder(validateJson[Place]) { request =>
    // `request.body` contains a fully validated `Place` instance.
    val place = request.body

    new Status(200)(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
    //Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))

  }

}