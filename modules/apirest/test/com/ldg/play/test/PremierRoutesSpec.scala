package com.ldg.play.test


import apirest.Routes
import com.ldg.play.baseclass.UnitSpec
import services.{MockTDataServices, TDataServices}

import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.inject.guice.GuiceApplicationBuilder

class PremierRoutesSpec extends UnitSpec {

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
