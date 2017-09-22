/**
  * Created by ldipotet on 13/7/17.
  */

package com.ldg.utils

import com.ldg.model.StatsTypes._
import com.ldg.model.Match
import com.ldg.model.Team.{AwayTeam, HomeTeam}
import play.api.libs.json._

import scala.io.Source

/**
  * For testing any regex: https://regex101.com
  */
class UtilitiesForCSV(file:String) {

  val csvMatchs = Source.fromFile(file)
  val linesOfCvsMatch:Seq[String] = csvMatchs.getLines().toSeq

  /**
    * Pattern for the following regex:
    * (,)(?=(?:[^}]|\{[^{]*})*$)
    *
    * any thing,"{any thing}",any thing,....
    *
    * only recognize every field separated by comma and ignore commas inside brackets
    * or any other punctuations mark
    *
   */

  def jsonbjectOfMatchPremier:Seq[JsObject] = linesOfCvsMatch.map(
    csvlines=>{
      val arrline=csvlines.split("(,)(?=(?:[^}]|\\{[^{]*})*$)")
      val (awayGoals,lisPlayerAway,awayTeamName,date,homeGoals,listPlayerHome,homeTeanName,season) =
        (arrline(0),arrline(1),arrline(2),arrline(3),arrline(4),arrline(5),arrline(6),arrline(7))

        Json.obj("awayTeamGoals" -> awayGoals,
                  "awayGoalPlayer" -> goalsPlayerListJsonObject(lisPlayerAway),
                  "matchAwayteam" -> awayTeamName.replaceAll("^\"|\"$",""),
                  "matchDate" -> date,
                  "homeTeamGoal" -> homeGoals,
                  "homeGoalsplayer" -> goalsPlayerListJsonObject(listPlayerHome),
                  "matchHometeam"  -> homeTeanName.replaceAll("^\"|\"$",""),
                  "season" -> season.replaceAll("^\"|\"$","")
        )
  }
 )

  def modelOfMatchPremier:Seq[Match] = linesOfCvsMatch.map(
    csvlines=>{
      val arrline=csvlines.split("(,)(?=(?:[^}]|\\{[^{]*})*$)")
      val (awayGoals,listPlayerAway,awayTeamName,date,homeGoals,listPlayerHome,homeTeamName,season) =
        (arrline(0),arrline(1),arrline(2),arrline(3),arrline(4),arrline(5),arrline(6),arrline(7))
      //(arrline(0),arrline(1),arrline(2),arrline(3),arrline(4),arrline(5),arrline(6),arrline(7))

      //Home Fields
      val tmpHomeTeamName = homeTeamName.replaceAll("^\"|\"$","")
      val homeGoalsPlayer = goalsPlayerList(listPlayerHome)

      val homeTeam = HomeTeam(tmpHomeTeamName,homeGoals.toInt,homeGoalsPlayer)

      //Away fields
      val tmpAwayTeamName = awayTeamName.replaceAll("^\"|\"$","")
      val awayGoalsPlayer:Map[playerName,List[minuteGoal]] = goalsPlayerList(listPlayerAway)

      val awayTeam = AwayTeam(tmpAwayTeamName,homeGoals.toInt,awayGoalsPlayer)

      Match(homeTeam,awayTeam,date,season.replaceAll("^\"|\"$",""))
    }
  )


/*
  case class Match(homeTeam: HomeTeam, awayTeam: AwayTeam, date: String, season : String)
  case class AwayTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])
  case class HomeTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])

*/

  def goalsPlayerListJsonObject:String =>JsValue = listplayer => {
  if (listplayer.length > 0)
    convertList(listplayer)
  else
    JsNull
}

  def goalsPlayerList:String =>Map[playerName,List[minuteGoal]] = listplayer => {

    if (listplayer.length > 0){
      val listPlayerMinutesPlayed:List[String] = listplayer.substring(2,listplayer.length-2).split("','").toList
      listOfTuples(listPlayerMinutesPlayed).toMap}
    else
      Map.empty[playerName,List[minuteGoal]]
  }

  def convertList(pattern:String): JsValue = {

    val listPlayerMinutesPlayed:List[String] = pattern.substring(2,pattern.length-2).split("','").toList

    val listOfTuples:List[(playerName,minuteGoal)] = listPlayerMinutesPlayed.map(
          playerAndMinutesPlayed => {

          val arrayPlayerMinutes= playerAndMinutesPlayed.split(":").toList

           //Pattern for the following Regex:
           //"(?m)^'|'$",""
          //remove simple apostrophe at the beginning and at the end of the word
          //pattern before 'string' after string
          val listPlayerMinutes:List[String] = arrayPlayerMinutes.map(playerOrMinutes=>playerOrMinutes.replaceAll("(?m)^'|'$",""))

          //replace all minutes '' per '. The pattern string'' per string '
          val (player,listOfMinutes):(playerName,minuteGoal)= (listPlayerMinutes.head,arrayPlayerMinutes.last.replaceAll("''","'"))

          (player,listOfMinutes)
        }
    )

    val finalMapPlayerVsMinutes:Map[playerName,minuteGoal] = listOfTuples.toMap

    Json.toJson(finalMapPlayerVsMinutes)

  }


  def listOfTuples(listPlayerMinutesPlayed:List[String]):List[(playerName,List[minuteGoal])] = listPlayerMinutesPlayed.map(
    playerAndMinutesPlayed => {

      val arrayPlayerMinutes:List[minuteGoal]= playerAndMinutesPlayed.split(":").toList

      //Pattern for the following Regex:
      //"(?m)^'|'$",""
      //remove simple apostrophe at the beginning and at the end of the word
      //pattern before 'string' after string
      val listPlayerMinutes:List[String] = arrayPlayerMinutes.map(playerOrMinutes=>playerOrMinutes.replaceAll("(?m)^'|'$",""))

      //replace all minutes '' per '. The pattern string'' per string '
      val (player,listOfMinutes):(playerName,minuteGoal)= (listPlayerMinutes.head,arrayPlayerMinutes.last.replaceAll("''","'"))

      (player,listOfMinutes.split(",").toList)
    }
  )

}

object UtilitiesForCSV {
  def apply(file: String) = {
    //new UtilitiesForCSV(file).jsonbjectOfMatchPremier
    new UtilitiesForCSV(file).modelOfMatchPremier

  }
}