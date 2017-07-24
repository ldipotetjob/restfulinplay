package com.ldg.model

import com.ldg.model.Team._

/**
  * Created by ldipotet on 29/6/17.
  *
  * Non Normalize CClass, just for for this Use case
  */
case class Match(homeTeam: HomeTeam, awayTeam: AwayTeam, date: String, season : String) {
  // construction reference =>
  // this.result = Integer.toString(homeTeam.getGoals())+"-"+Integer.toString(awayTeam.getGoals());
  def result: Int = homeTeam.goals + awayTeam.goals
}