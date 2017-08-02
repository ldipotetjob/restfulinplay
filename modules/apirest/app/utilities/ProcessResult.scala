package utilities


import javax.inject.Inject

import play.api.mvc._
import controllers.PremierleagueController
import play.api.http.MimeTypes
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Accepting, Result, Rendering}
import play.api.mvc.Results.Status


/**
  * Created by ldipotet on 31/7/17.
  */
//class ProcessResult@Inject() (controller: PremierleagueController) {
/*object ProcessResult {


  val AcceptsCSV = Accepting("txt/csv")

  val AcceptsJson = Accepting(MimeTypes.JSON)

  def processRequest[T, R](rendering:Rendering)(block: T => R): Result = {

    rendering.render {
      case AcceptsCSV => new Status(200)(Json.obj("status" -> "OK", "message" -> ("Place saved.")))
      case AcceptsJson => new Status(200)(Json.obj("status" -> "OK", "message" -> ("Place saved.")))
    }
 }
}*/

/*
trait ProcessResult {

  self : PremierleagueController =>

  def processRequest(implicit request: Request[AnyContent] => Result ): Result = {
    val AcceptsCSV = Accepting("txt/csv")

    val AcceptsJson = Accepting(MimeTypes.JSON)

    render {
      case AcceptsCSV => new Status(200)(Json.obj("status" -> "OK", "message" -> ("Place saved.")))
      case AcceptsJson => new Status(200)(Json.obj("status" -> "OK", "message" -> ("Place saved.json")))
    }
  }
}*/