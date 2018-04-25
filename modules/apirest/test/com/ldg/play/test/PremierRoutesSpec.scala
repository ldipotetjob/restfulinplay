package com.ldg.play.test

import akka.stream.Materializer

import org.scalatest.{FlatSpec, Matchers}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import play.api.inject.guice.GuiceApplicationBuilder

class PremierRoutesSpec extends FlatSpec with Matchers {

  val app: play.api.Application = new GuiceApplicationBuilder().build()
  implicit lazy val materializer: Materializer = app.materializer

   "Request /GET/premier/PRML/matchs with Content-Type:text/plain and application/json" should
    "return a json file Response with a 200 Code" in {
    val Some(result) = route(app, FakeRequest(GET, "/apirest/premier/matchs")
      .withHeaders(("Accept", "application/json"),("Content-Type", "text/plain")))
    status(result) shouldBe OK
  }
}
