/**
  * Created by ldipotet on 19/9/17.
  */

package com.ldg.model.test

import com.ldg.model.StatsTypes._
import com.ldg.model.TestMatchObject._
import com.ldg.implicitconversions.ImplicitConvertions._


import org.scalatest.{Matchers, FlatSpec}

import play.api.libs.json._

/**
  *
  * Testing right convertion from a Model => Json
  *
  */

class ModelConverterSpec extends FlatSpec with Matchers{

  val jsonStringValue = """
  {
    "date"          : "1495502158120",
    "season"        : "MW 22",
    "homeTeam"      : {
      "name"        :"Stoke City",
      "goals"        : 1,
      "goalsPlayer" : {
                     "Juan Mata": ["19''"]
       }
    },
      "awayTeam"    : {
      "name"        :"Manchester United",
      "goals"        : 1,
      "goalsPlayer" : {
                          "Wayne Rooney": ["90 +4''"]
        }
      }
 }
    """

  //TODO test away team example BUT it a boilerplate code

  val jsonValueMatch:JsValue = Json.toJson(matchGame)

  val jsonExpectedValue:JsValue = Json.parse(jsonStringValue)


  "Dates and season " should "be parsed correctly" in {

    (jsonValueMatch \ "date").get should equal ((jsonExpectedValue \ "date").get)
    (jsonValueMatch \ "season").get should equal ((jsonExpectedValue \ "season").get)

  }

  "TeamNames and goals " should "be parsed correctly" in {

    (jsonValueMatch \ "homeTeam" \ "name").get should equal ((jsonExpectedValue \ "homeTeam" \ "name").get)
    (jsonValueMatch \ "homeTeam" \ "goals").get should equal ((jsonExpectedValue \ "homeTeam" \ "goals").get)

    (jsonValueMatch \ "awayTeam" \ "name").get should equal ((jsonExpectedValue \ "awayTeam" \ "name").get)
    (jsonValueMatch \ "awayTeam" \ "goals").get should equal ((jsonExpectedValue \ "awayTeam" \ "goals").get)

  }


  "Player and it's goals " should "be parsed correctly" in {

    //In test is preferable use .as instead of .asOpt because Prefer that the test fail in place of process that Option Result

    val jsonValueMatchHomeTeamGoalsPlayer:Map[playerName,List[minuteGoal]] = (jsonValueMatch \ "homeTeam" \ "goalsPlayer" ).as[Map[playerName,List[minuteGoal]]]
    val jsonValueMatchAwayTeamGoalsPlayer:Map[playerName,List[minuteGoal]] = (jsonValueMatch \ "awayTeam" \ "goalsPlayer" ).as[Map[playerName,List[minuteGoal]]]

    val jsonExpectedValueHomeTeamGoalsPlayer:Map[playerName,List[minuteGoal]] = (jsonValueMatch \ "homeTeam" \ "goalsPlayer" ).as[Map[playerName,List[minuteGoal]]]
    val jsonExpectedValueAwayTeamGoalsPlayer:Map[playerName,List[minuteGoal]] = (jsonValueMatch \ "awayTeam" \ "goalsPlayer" ).as[Map[playerName,List[minuteGoal]]]

    jsonValueMatchHomeTeamGoalsPlayer.size should equal ( jsonExpectedValueHomeTeamGoalsPlayer.size)
    jsonValueMatchHomeTeamGoalsPlayer("Juan Mata") should equal ( jsonExpectedValueHomeTeamGoalsPlayer("Juan Mata"))

  }
}