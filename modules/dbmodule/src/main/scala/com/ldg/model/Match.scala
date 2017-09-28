/**
  * Created by ldipotet on 29/6/17.
  */

package com.ldg.model

import com.ldg.model.Team._

/**
  * Created by ldipotet on 29/6/17.
  *
  * Non Normalize CClass, just for this Use case
  */

sealed trait Parseable

case class Match(homeTeam: HomeTeam, awayTeam: AwayTeam, date: String, season : String) extends Parseable{
  // construction reference =>
  // this.result = Integer.toString(homeTeam.getGoals())+"-"+Integer.toString(awayTeam.getGoals());
  def result: Int = homeTeam.goals + awayTeam.goals
}

object TestMatchObject {

  val awayTeam = AwayTeam("Manchester United",1,Map("Wayne Rooney"->List("90 +4''")))
  val homeTeam = HomeTeam("Stoke City",1,Map("Juan Mata"->List("19''")))
  val matchGame = Match(homeTeam,awayTeam,"1495502158120","MW 22")

}

