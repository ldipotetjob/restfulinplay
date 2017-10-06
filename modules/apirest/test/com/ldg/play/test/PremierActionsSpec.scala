package com.ldg.play.test



import akka.stream.Materializer
import org.scalatest.{FlatSpec, Matchers}
import play.api.test.FakeRequest
import play.api.libs.json._
import play.api.test.Helpers._

import scala.io.Source
import com.ldg.basecontrollers.{BaseController, JsonActionBuilder}

import com.ldg.implicitconversions.ImplicitConversions.matchReads

import com.ldg.model.Match

import org.scalatest.mock.MockitoSugar
import play.api.http.Status.OK

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results.Status



class PremierActionsSpec extends FlatSpec with Matchers  with MockitoSugar{

  val jsonActionBuilder = new JsonActionBuilder()

  val jsonGenericAction = new BaseController().JsonAction[Match](matchReads)

  val rightMatchJson = Source.fromURL(getClass.getResource("/rightmatch.json")).getLines.mkString

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

  "Request /POST/ to JsonActionBuilder with Content-Type:application/json and a right Json body" should "return a 200 Code" in {

    val request = FakeRequest(POST, "/").withJsonBody(Json.parse(rightMatchJson)).withHeaders(("Content-Type", "application/json"))

    def action = jsonActionBuilder{ implicit request =>

      new Status(OK)

    }

    val result = call(action, request)

    status(result) shouldBe OK

  }

  "Request /POST/ to JsonAction with Content-Type:application/json - right Json body - " should "return a 200 Code" in {

    val request = FakeRequest(POST, "/").withJsonBody(Json.parse(rightMatchJson)).withHeaders(("Content-Type", "application/json"))

    def action = jsonGenericAction{ implicit request =>

      new Status(OK)

    }

    val result = call(action, request)

    status(result) shouldBe OK

  }





}