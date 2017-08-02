package utilities

import com.ldg.model.StatsTypes.{minuteGoal, playerName}
import play.api.libs.json._

import scala.io.Source

/**
  * Created by ldipotet on 13/7/17.
  *
  * for testing any regex: https://regex101.com
  *
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
                  "awayGoalPlayer" -> goalsPlayerList(lisPlayerAway),
                  "matchAwayteam" -> awayTeamName.replaceAll("^\"|\"$",""),
                  "matchDate" -> date,
                  "homeTeamGoal" -> homeGoals,
                  "homeGoalsplayer" -> goalsPlayerList(listPlayerHome),
                  "matchHometeam"  -> homeTeanName.replaceAll("^\"|\"$",""),
                  "season" -> season.replaceAll("^\"|\"$","")
        )
  }
 )

def goalsPlayerList:String =>JsValue = listplayer => {
  if (listplayer.length > 0)
    convertList(listplayer)
  else
    JsNull
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

}

object UtilitiesForCSV {
  def apply(file: String) = {
    new UtilitiesForCSV(file).jsonbjectOfMatchPremier
  }
}