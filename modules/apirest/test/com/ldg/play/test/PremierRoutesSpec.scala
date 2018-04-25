package com.ldg.play.test

import akka.stream.Materializer
import apirest.Routes
import org.scalatest.{FlatSpec, Matchers}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.inject.guice.GuiceApplicationBuilder
import services.{MockTDataServices, TDataServices}

class PremierRoutesSpec extends FlatSpec with Matchers {

  val app = new GuiceApplicationBuilder()
    .overrides(bind[TDataServices].to[MockTDataServices])
    .configure("play.http.router" -> classOf[Routes].getName)
    .build()

 // implicit lazy val materializer: Materializer = app.materializer

   "Request /GET/premier/PRML/matchs with Content-Type:text/plain and application/json" should
    "return a json file Response with a 200 Code" in {
    val Some(result) = route(app, FakeRequest(GET, "/premier/matchs")
      .withHeaders(("Accept", "application/json"),("Content-Type", "text/plain")))
    status(result) shouldBe OK
  }
}
