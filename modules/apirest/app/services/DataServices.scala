package services

import com.ldg.model.Match
import com.ldg.model.StatsTypes.{minuteGoal, playerName}
import com.ldg.model.Team.{AwayTeam, HomeTeam}
import play.api.libs.json.{JsNull, JsObject, JsValue, Json}

import scala.io.Source

/** Implementation of Proof Of Concept for Simulate a Service*/
class DataServices extends TDataServices {

  /**
    * Traverse a text file
    * @param file
    * @return Seq of text file lines
    */
  def calclinesOfCvsMatch(file: String): Seq[String] = {
    val csvMatchs = Source.fromFile(file)
    csvMatchs.getLines().toSeq
  }

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

  /**
    * For a text file => every line is converted to a JsObject
    *
    * @param file
    * @return
    */
  def jsonbjectOfMatchFootball(file: String): Seq[JsObject] = {

    val linesOfCvsMatch = calclinesOfCvsMatch(file)
    linesOfCvsMatch.map(
      csvlines => {
        val arrline = csvlines.split("(,)(?=(?:[^}]|\\{[^{]*})*$)")
        val (awayGoals, lisPlayerAway, awayTeamName, date, homeGoals, listPlayerHome, homeTeanName, season) =
          (arrline(0), arrline(1), arrline(2), arrline(3), arrline(4), arrline(5), arrline(6), arrline(7))

        Json.obj("awayTeamGoals" -> awayGoals,
          "awayGoalPlayer" -> goalsPlayerListJsonObject(lisPlayerAway),
          "matchAwayteam" -> awayTeamName.replaceAll("^\"|\"$", ""),
          "matchDate" -> date,
          "homeTeamGoal" -> homeGoals,
          "homeGoalsplayer" -> goalsPlayerListJsonObject(listPlayerHome),
          "matchHometeam" -> homeTeanName.replaceAll("^\"|\"$", ""),
          "season" -> season.replaceAll("^\"|\"$", "")
        )
      }
    )
  }
  /** Service that when is call return a seq of Match game Model*/
  def modelOfMatchFootball(file: String="football.txt"): Seq[Match] = {
    val linesOfCvsMatch = calclinesOfCvsMatch(file)
    linesOfCvsMatch.map(
      csvlines => {
        val arrline = csvlines.split("(,)(?=(?:[^}]|\\{[^{]*})*$)")
        val (awayGoals, listPlayerAway, awayTeamName, date, homeGoals, listPlayerHome, homeTeamName, season) =
          (arrline(0), arrline(1), arrline(2), arrline(3), arrline(4), arrline(5), arrline(6), arrline(7))

        /** Home Fields */
        val tmpHomeTeamName = homeTeamName.replaceAll("^\"|\"$", "")
        val homeGoalsPlayer = goalsPlayerList(listPlayerHome)

        val homeTeam = HomeTeam(tmpHomeTeamName, homeGoals.toInt, homeGoalsPlayer)

        /** Away fields */
        val tmpAwayTeamName = awayTeamName.replaceAll("^\"|\"$", "")
        val awayGoalsPlayer: Map[playerName, List[minuteGoal]] = goalsPlayerList(listPlayerAway)

        val awayTeam = AwayTeam(tmpAwayTeamName, homeGoals.toInt, awayGoalsPlayer)

        Match(homeTeam, awayTeam, date, season.replaceAll("^\"|\"$", ""))
      }
    )
  }

  def goalsPlayerListJsonObject: String => JsValue = listplayer => {
    if (listplayer.length > 0)
      convertList(listplayer)
    else
      JsNull
  }

  def goalsPlayerList: String => Map[playerName,List[minuteGoal]] = listplayer => {
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

        /**
          * Pattern for the following Regex:
          * "(?m)^'|'$",""
          * remove simple apostrophe at the beginning and at the end of the word
          * pattern before 'string' after string
          */
        val listPlayerMinutes:List[String] = arrayPlayerMinutes
          .map(playerOrMinutes=>playerOrMinutes.replaceAll("(?m)^'|'$",""))

        /** replace all minutes '' per '. The pattern string'' per string '*/
        val (player,listOfMinutes):(playerName,minuteGoal)=
          (listPlayerMinutes.head,arrayPlayerMinutes.last.replaceAll("''","'"))
        (player,listOfMinutes)
      }
    )
    val finalMapPlayerVsMinutes:Map[playerName,minuteGoal] = listOfTuples.toMap
    Json.toJson(finalMapPlayerVsMinutes)
  }

  def listOfTuples(listPlayerMinutesPlayed: List[String]): List[(playerName,List[minuteGoal])] =
    listPlayerMinutesPlayed.map(
    playerAndMinutesPlayed => {
      val arrayPlayerMinutes:List[minuteGoal]= playerAndMinutesPlayed.split(":").toList

      //Pattern for the following Regex:
      //"(?m)^'|'$",""
      //remove simple apostrophe at the beginning and at the end of the word
      //pattern before 'string' after string
      val listPlayerMinutes:List[String] = arrayPlayerMinutes
        .map(playerOrMinutes=>playerOrMinutes.replaceAll("(?m)^'|'$",""))
      //replace all minutes '' per '. The pattern string'' per string '
      val (player,listOfMinutes):(playerName,minuteGoal)=
        (listPlayerMinutes.head,arrayPlayerMinutes.last.replaceAll("''","'"))
      (player,listOfMinutes.split(",").toList)
    }
  )

}