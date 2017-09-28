/**
  * Created by ldipotet on 19/9/17.
  */

package com.ldg.model.test

import com.ldg.model.StatsTypes._
import org.scalatest.{FlatSpec, Matchers}
import com.ldg.implicitconversions.ImplicitConvertions._
import play.api.libs.json._
import com.ldg.model.Match

/**
  *
  * Testing right convertion from a Json => Model
  *
  */

class jsonConverterTest extends FlatSpec with Matchers {

//TODO Test for convertion of Place Object

  val jsonPlace ="""
  {
    "name" : "Watership Down",
    "location" : {
      "lat" : 51.235685,
      "long" : -1.309197
    },
    "residents" : [ {
      "name" : "Fiver",
      "age" : 4,
      "role" : null
    }, {
      "name" : "Bigwig",
      "age" : 6,
      "role" : "Owsla"
    } ]
  }
  """

  val jsonMatch ="""
  {
    "date" : "1495502158120",
    "season" : "MW 26",
    "homeTeam" :{
      "name" : "Aston Villa",
      "goals" : 3,
      "goalsPlayer" : {
         "Dean Saunders": ["44''", "66''"],
         "Garry Parker": ["78''"]
          }
    },
     "awayTeam" : {
      "name" : "Liverpool",
      "goals" : 3,
      "goalsPlayer" : {
        "Mark Walters": ["43''"],
        "Ronnie Rosenthal": ["84''"],
        "John Williams": ["90''"]
        }
     }
  }
  """

  val jsvaluePlace: JsValue = Json.parse(jsonPlace)
  val jsvalueMatch: JsValue = Json.parse(jsonMatch)
  val awayMapsTeamGoalPlayer:Map[playerName,List[minuteGoal]] =  jsvalueMatch.as[Match].awayTeam.goalsPlayer


  val homeMapsTeamGoalPlayer:Map[playerName,List[minuteGoal]] =  jsvalueMatch.as[Match].homeTeam.goalsPlayer


  it should "produce a MatchGame model from it's equivalent Json" in {
    jsvalueMatch.validate[Match] shouldBe a[JsSuccess[_]]
    //jsvalueMatch.validate[Match] shouldBe a [Match] // Ensure an object is an instance of a specified class or trait
  }

  "A non-empty FootballHomeMapPlayer" should "have the correct size" in { // Describe the MapFootballPlayer size
    assert(homeMapsTeamGoalPlayer.size == 2)
  }

  "Goals and TeamNames " should "be parsed correctly" in {

    val awayTeamName: teamName = jsvalueMatch.as[Match].awayTeam.name
    val awayGoalTeam: Int = jsvalueMatch.as[Match].awayTeam.goals

    val homeTeamName: teamName = jsvalueMatch.as[Match].homeTeam.name
    val homeGoalTeam: Int = jsvalueMatch.as[Match].homeTeam.goals

    homeTeamName should equal("Aston Villa")
    homeGoalTeam should equal(3)
    awayTeamName should equal("Liverpool")
    awayGoalTeam should equal(3)

  }

  "A non-empty FootballAwayMapPlayer" should "have the correct size" in { // Describe the MapFootballPlayer size
    assert(awayMapsTeamGoalPlayer.size == 3)
  }

}