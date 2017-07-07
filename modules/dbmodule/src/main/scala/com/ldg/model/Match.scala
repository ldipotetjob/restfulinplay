package com.ldg.model

import com.ldg.model.Team._

/**
  * Created by ldipotet on 29/6/17.
  */
case class Match(matchNunmber: Int, homeTeam: HomeTeam, awayTeam: AwayTeam, date: String, season : String) {
  // construction reference =>
  // this.result = Integer.toString(homeTeam.getGoals())+"-"+Integer.toString(awayTeam.getGoals());
  def result: Int = homeTeam.goals + awayTeam.goals
}