package utilities


import play.api.http._
import play.api.mvc._

trait ContentNegotiation {

  //Self type annotation -> trait must be mixed in into Controller (thus, has access to render, Accepts and Ok)
  self : Controller =>

  //Serializes the provided value according to the Accept headers of the (implicitly) provided request
  def proccessContentNegotiation[C](content: C)(implicit request: Request[AnyContent], writeable: Writeable[C]) : Result = {
    val AcceptsCSV = Accepting("txt/csv")
    render {
      case Accepts.Json() => Ok(content).withHeaders(CONTENT_TYPE->JSON)
      case AcceptsCSV => Ok(content).withHeaders(CONTENT_TYPE->"text/cvs")
      case _ => NotAcceptable("")

    }
  }
}