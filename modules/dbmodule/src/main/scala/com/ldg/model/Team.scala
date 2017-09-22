/**
  * Created by ldipotet on 29/6/17.
  */

package com.ldg.model

import com.ldg.model.StatsTypes._


object Team {

  case class AwayTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])
  case class HomeTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])
  case class TeamPerSeason(season:String,teamName:teamName,url:urlLink)

}
