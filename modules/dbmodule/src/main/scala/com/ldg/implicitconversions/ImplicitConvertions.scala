/**
  * Created by ldipotet on 29/6/17.
  */

package com.ldg.implicitconversions

import com.ldg.model.Match
import com.ldg.model.Team.{AwayTeam, HomeTeam}
import play.api.libs.json.{Format, Json, Reads}

object ImplicitConvertions {

  implicit val awayTeamFormat: Format[AwayTeam] = Json.format[AwayTeam]
  implicit val homeTeamFormat: Format[HomeTeam] = Json.format[HomeTeam]
  implicit val matchGameFormat: Format[Match] = Json.format[Match]
  implicit val matchReads:Reads[Match] = Json.reads[Match]

}
