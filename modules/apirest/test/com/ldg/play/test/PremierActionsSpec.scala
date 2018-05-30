package com.ldg.play.test

import akka.stream.Materializer
import com.ldg.basecontrollers.{BaseController, DefaultActionBuilder, JsonActionBuilder}
import com.ldg.implicitconversions.ImplicitConversions.matchReads
import com.ldg.model.Match
import com.ldg.play.baseclass.UnitSpec
import play.api.test.FakeRequest
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.http.Status.OK
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results.Status
import scala.io.Source

class PremierActionsSpec extends UnitSpec {

  val jsonActionBuilder = new JsonActionBuilder()
  val defaultActionBuilder = new DefaultActionBuilder()
  val jsonGenericAction = new BaseController().JsonAction[Match](matchReads)

  val rightMatchJson = Source.fromURL(getClass.getResource("/rightmatch.json")).getLines.mkString
  val wrongMatchJson = Source.fromURL(getClass.getResource("/wrongmatch.json")).getLines.mkString

  implicit lazy val app: play.api.Application = new GuiceApplicationBuilder().configure().build()
  implicit lazy val materializer: Materializer = app.materializer

  /**
    * Test JsonActionBuilder:
    *
    * validate: content-type
    *           jsonBody must be specific Model
    *
    * @see com.ldg.basecontrollers.JsonActionBuilder
    *
    * Request: application/json
    *
    */

  "JsonActionBuilder with Content-Type:application/json and a right Json body" should "return a 200 Code" in {
    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(rightMatchJson))
      .withHeaders(("Content-Type", "application/json"))

    def action = jsonActionBuilder{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe OK
  }

  "JsonActionBuilder with Wrong Content-Type and a right Json body" should "return a 403 Code" in {
    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(rightMatchJson))
      .withHeaders(("Content-Type", "text/html"))

    def action = jsonActionBuilder{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe FORBIDDEN
  }

  "JsonActionBuilder with Content-Type:application/json and a wrong Json body" should "return a 400 Code" in {
    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(wrongMatchJson))
      .withHeaders(("Content-Type", "application/json"))

    def action = jsonActionBuilder{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe BAD_REQUEST
  }

  "JsonAction with Content-Type:application/json and a wrong Json body" should "return a 400 Code" in {
    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(wrongMatchJson))
      .withHeaders(("Content-Type", "application/json"))

    def action = jsonGenericAction{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe BAD_REQUEST
  }

  "JsonAction with Content-Type:application/json and a right Json body" should "return a 200 Code" in {
    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(rightMatchJson))
      .withHeaders(("Content-Type", "application/json"))

    def action = jsonGenericAction{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe OK
  }

  "JsonAction with Wrong Content-Type and a right Json body" should "return a 403 Code" in {

    val request = FakeRequest(POST, "/")
      .withJsonBody(Json.parse(rightMatchJson))
      .withHeaders(("Content-Type", "text/html"))

    def action = jsonGenericAction{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe UNSUPPORTED_MEDIA_TYPE
  }

  "DefaultActionBuilder with Content-Type:text/plain and a right Json body" should "return a 200 Code" in {
    val request = FakeRequest(GET, "/").withHeaders(("Accept","application/json"),("Content-Type", "text/plain"))

    def action = defaultActionBuilder{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe OK
  }

  "DefaultActionBuilder with wrong Content-Type:application/json " should "return a 403 Code" in {
    val request = FakeRequest(GET, "/").withHeaders(("Accept","application/json"),("Content-Type", "application/json"))

    def action = defaultActionBuilder{ implicit request =>
      new Status(OK)
    }

    val result = call(action, request)
    status(result) shouldBe FORBIDDEN
  }

}


