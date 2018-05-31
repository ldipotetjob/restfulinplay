/**
  * Created by ldipotet on 13/7/17.
  */

package controllers

import javax.inject._

import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import com.ldg.basecontrollers.{BaseController, ContentNegotiation, DefaultControllerComponents}
import com.ldg.implicitconversions._
import com.ldg.implicitconversions.MyOwnConversions._
import com.ldg.implicitconversions.ImplicitConversions._
import com.ldg.model._
import com.ldg.security.ApiDataHandler
import services.TDataServices
import scala.concurrent._
import scalaoauth2.provider._

@Singleton
class FootballleagueControllerWithoAuth2 @Inject()(action: DefaultControllerComponents, services: TDataServices)
  extends BaseController
  with ContentNegotiation
  with OAuth2Provider{

  /*************************LifeCycle*******************************

     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+

    ******************************************************************/

  /********************** EndPoint explanations ***************************

   The authorization process utilizes two authorization server endpoints
   (HTTP resources):

   o  Authorization endpoint - used by the client to obtain
      authorization from the resource owner via user-agent redirection.

   o  Token endpoint - used by the client to exchange an authorization
      grant for an access token, typically with client authentication.

   As well as one client endpoint:

   o  Redirection endpoint - used by the authorization server to return
      responses containing authorization credentials to the client via
      the resource owner user-agent.


   Not every authorization grant type utilizes both endpoints.
   Extension grant types MAY define additional endpoints as needed.!

    *************************************************************************/


  /**
    * for test this POST request
    * curl -d '{"date" : "1495502158120","season" : "MW 26","homeTeam" :{"name" : "Aston Villa","goals" : 3,"goalsPlayer" : {"Dean Saunders": ["44''", "66''"],"Garry Parker": ["78''"]}},"awayTeam" : {"name" : "Liverpool","goals" : 3,"goalsPlayer" : {"Mark Walters": ["43''"],"Ronnie Rosenthal": ["84''"],"John Williams": ["90''"]}}}'  -H "Content-Type: application/json" http://localhost:9000/apirest/football/match;echo
    *
    * other solution more elegant is put the json in a file and type de following command in your terminal:
    *
    * curl -d "@myJasonFile.json"  -H "Content-Type: application/json" http://localhost:9000/apirest/football/match ; echo
    *
    */

  /**
    * Best way, using a wrapper for a action that process a POST with a JSON in the request.
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    * @return specific Result when every things is Ok so we send the status and the comment with the specific
    *         json that show the result
    */


    def insertMatchWithCustomized = action.jsonActionBuilder{ implicit request =>
    val matchGame: Match = request.body.asJson.get.as[Match]
      processContentNegotiationForJson[Match](matchGame)
  }

  /**
    *
    * for test this action
    * curl -d '{"date" : "1495502158120","season" : "MW 26","homeTeam" :{"name" : "Aston Villa","goals" : 3,"goalsPlayer" : {"Dean Saunders": ["44''", "66''"],"Garry Parker": ["78''"]}},"awayTeam" : {"name" : "Liverpool","goals" : 3,"goalsPlayer" : {"Mark Walters": ["43''"],"Ronnie Rosenthal": ["84''"],"John Williams": ["90''"]}}}'  -H "Content-Type: application/json" http://localhost:9000/apirest/football/matchgame;echo
    *
    */
  /**
    *
    * matchReads: implicit Reads=> Reads[Match]
    * @see com.ldg.implicitconversions.ImplicitConvertions.matchReads
    *
    */

  def insertMatchGeneric = JsonAction[Match](matchReads){ implicit request =>
    val matchGame: Match = request.body.asJson.get.as[Match]
    processContentNegotiationForJson[Match](matchGame)
  }

  /**
    *
    * for testing GET request:
    *
    * curl -H "Accept: application/json" -H "Content-Type: text/plain" http://localhost:9000/apirest/football/matchs ; echo
    *
    * curl -H "Accept: text/csv" -H "Content-Type: text/plain" http://localhost:9000/apirest/football/matchs ; echo
    *
    */

  def getMatchGame = action.defaultActionBuilder.async { implicit request =>
    authorize(new ApiDataHandler()) { authInfo =>
      val dataResults: Seq[Match] = services.modelOfMatchFootball("football.txt")
      //Future.successful(proccessContentNegotiation[Match](dataResults))
      proccessContentNegotiation[Match](dataResults)
    }
  }

  def myAction = Action.async { implicit req =>
    val value = "dgd"
    render.async {
      case Accepts.Html() => Future.successful(Ok(views.html.index("dss")))
      case Accepts.Json() => Future.successful(Ok(Json.toJson(value)))
    }
  }

/*
  def savePlaceConcise1 = Action.async(validateJson[Place]) { request =>
    authorize(new ApiDataHandler()) { authInfo =>
      val place = request.body
      Future.successful(new Status(200)(Json.obj("status" -> "OK", "message" -> ("Place '" + place.name + "' saved."))))
    }
  }
*/
  /**
    * The simplest way for process a request, in this case we call a default action builder
    * and process inside the action converting a body in a json, if something goes wrong we have
    * an NONE and therefore a BadRequest in other case a OK (200) of Response is get back.
    *
    * Action NOT registered in the route file. It's just for show how modify the HTTP header
    * some responses . If you want to test it you will need to add it in
    * the route file.
    *
    */

  def save = action.defaultActionBuilder{ request =>
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
    * A way for process a Json request, using a base action and  an specific BodyParser validator for the JSON,
    * if something goes wrong the same Body parser Validator throw the BadRequest in other case an OK (200) of Response
    * is returned.
    *
    * @see utilities.DefaultControllerComponents.jsonActionBuilder a reference to JsonActionBuilder
    *      a builder of actions that process POST request
    * @return Result (Ok/BadRequest)
    */

  def savePlaceConcise = Action(validateJson[Place]) { request =>
    val place = request.body
    new Status(200)(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
  }

  override val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      OAuthGrantType.AUTHORIZATION_CODE -> new AuthorizationCode(),
      OAuthGrantType.REFRESH_TOKEN -> new RefreshToken(),
      OAuthGrantType.CLIENT_CREDENTIALS -> new ClientCredentials(),
      OAuthGrantType.PASSWORD -> new Password()
    )
  }

  def accessToken = Action.async { implicit request =>
    issueAccessToken(new ApiDataHandler())
  }

}