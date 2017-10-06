package com.ldg.play.test



import akka.stream.Materializer
import org.scalatest.{FlatSpec, Matchers}
import play.api.test.FakeRequest
import play.api.libs.json._
import play.api.test.Helpers._

import scala.io.Source
import com.ldg.basecontrollers.{DefaultActionBuilder, DefaultControllerComponents, JsonActionBuilder}
import com.ldg.implicitconversions.ImplicitConversions._
import com.ldg.model.Match
import play.api.http.Status.OK
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results.Status


class PremierActionsSpec extends FlatSpec with Matchers {

  val jsonActionBuilder = new JsonActionBuilder()
  val mockDefaultActionBuilder = new DefaultActionBuilder()

  val rightMatchJson = Source.fromURL(getClass.getResource("/rightmatch.json")).getLines.mkString

  implicit lazy val app: play.api.Application = new GuiceApplicationBuilder().configure().build()
  implicit lazy val materializer: Materializer = app.materializer

  /**
    *
    * Testing right response for acceptance of application/header
    * Request: plain text
    * Response: a Json
    *
    */

  "Request /POST/ with Content-Type:application/json and a right Json body" should "return a 200 Code" in {

    val request = FakeRequest(POST, "/").withJsonBody(Json.parse(rightMatchJson)).withHeaders(("Content-Type", "application/json"))

    def action = jsonActionBuilder{ implicit request =>

      val matchGame: Match = request.body.asJson.get.as[Match]

      new Status(OK)
    }
    val result = call(action, request)

    status(result) shouldBe OK

  }

}