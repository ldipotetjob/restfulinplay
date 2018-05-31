/**
  * Created by ldipotet on 23/09/17.
  */
package com.ldg.play.test


import com.ldg.basecontrollers.{DefaultActionBuilder, DefaultControllerComponents, JsonActionBuilder}
import com.ldg.model.TestMatchObject._
import com.ldg.implicitconversions.ImplicitConversions._
import com.ldg.model.Match
import com.ldg.play.baseclass.UnitSpec
import controllers.FootballLeagueController
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.i18n._
import services.TDataServices


class FootballControllerSpec extends UnitSpec with MockitoSugar {

  val mockDataServices = mock[TDataServices]
  val mockDefaultControllerComponents = mock[DefaultControllerComponents]
  val mockActionBuilder = new JsonActionBuilder()
  val mockDefaultActionBuilder = new DefaultActionBuilder()

  val langs = new Langs {
    def availables: Seq[Lang] = Seq(Lang("fr"), Lang("en"),Lang("de") )
    def preferred(candidates: Seq[Lang]): Lang = Lang("fr")
  }

  val TestDefaultControllerComponents: DefaultControllerComponents = DefaultControllerComponents(mockDefaultActionBuilder,mockActionBuilder,/*messagesApi,*/langs)
  val TestFootballleagueController = new FootballLeagueController(TestDefaultControllerComponents,mockDataServices)

  /**
    *
    * Testing right response for acceptance of application/header
    * Request: plain text
    * Response: a Json
    *
    */

  "Request /GET/ with Content-Type:text/plain and application/json" should
    "return a json file Response with a 200 Code" in {

      when(mockDataServices.modelOfMatchFootball("football.txt")) thenReturn Seq(matchGame)
      val request = FakeRequest(GET, "/football/matchs")
        .withHeaders(("Accept","application/json"),("Content-Type","text/plain"))
      val result = TestFootballleagueController.getMatchGame(request)
      val resultMatchGame: Match = (contentAsJson(result) \ "message" \ 0).as[Match]
      status(result) shouldBe OK
      resultMatchGame.homeTeam.name should equal(matchGame.homeTeam.name)
      resultMatchGame.homeTeam.goals should equal(matchGame.homeTeam.goals)

    }

  /**
    *
    * Testing right response for acceptance of application/header
    * Testing  template work fine: Result of a mocked template shoulBe equal to Res
    * Request: plain text
    * Response: a CSV file
    *
    */

  "Request /GET/ with Content-Type:text/plain and txt/csv" should "return a csv file Response with a 200 Code" in {
    when(mockDataServices.modelOfMatchFootball("football.txt")) thenReturn Seq(matchGame)
    val request = FakeRequest(GET, "/football/matchs").withHeaders(("Accept","text/csv"),("Content-Type","text/plain"))
    val result = TestFootballleagueController.getMatchGame(request)
    val content = views.csv.football(Seq(matchGame))
    val templateContent: String = contentAsString(content)
    val resultContent: String = contentAsString(result)
    status(result) shouldBe OK
    templateContent should equal(resultContent)
  }

  "Request /GET with Content-Type:text/plain and WRONG Accept: image/jpeg" should
    "return a json file Response with a 406 Code" in {
    val result = TestFootballleagueController.getMatchGame
      .apply(FakeRequest(GET, "/").withHeaders(("Accept","image/jpeg"),("Content-Type","text/plain")))
    status(result) shouldBe NOT_ACCEPTABLE
  }
}