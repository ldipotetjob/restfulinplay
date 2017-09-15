package controllers

import javax.inject._
import akka.util.ByteString
import com.ldg.model.SamplePlayer
import play.api._
import play.api.http.HttpEntity
import play.api.libs.json._
import play.api.mvc._
import utilities.{Resident, Location, Place,Record}
import utilities.MyOwnConversions._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {


  /**
    * Action NOT registered in the route file. It's just for show how modify in
    * some responses the HTTP header. If you want to test it you will need to add it in
    * the route file.
    *
    */

  def indexWithHeader = Action {
    Ok("Hello World, i' here!").withHeaders(
      CACHE_CONTROL -> "max-age=3600",
      ETAG -> "xx")
  }

  val places: List[Place] = List(Place(
    "Watership Down",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Fiver", 4, None),
      Resident("Bigwig", 6, Some("Owsla"))
    )
  ),Place(
    "Brigthon",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Lous", 4, None),
      Resident("Berguen", 6, Some("Owsla"))
    )
  ))


  /**
    * Action NOT registered in the route file. It's just for show how to check some http request tag
    * and depending of specific value make one kind of response or another, in this case we will check the session.
    * If you want to test it you will need to add it at the route file.
    */

  def indexCheckingSession = Action {
    request => request.session.get("connected").map { user =>
      Ok("Hello " + user)
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }

  /**
    * Action NOT registered in the route file. It's just for show how modify
    * some responses the response body. If you want to test it you will need to add it in
    * the route file.
    *
    */

  def indexModifiyingBody = Action {
    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Strict(ByteString("Hello world!"), Some("text/plain"))
    )
  }

}
