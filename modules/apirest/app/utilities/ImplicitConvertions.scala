package utilities

import com.ldg.model.Team.{AwayTeam, HomeTeam}
import play.api.libs.json.{Json, Format}
import com.ldg.model.Match

/**
  * Created by ldipotet on 29/6/17.
  */
object ImplicitConvertions {

  implicit val awayTeamFormat: Format[AwayTeam] = Json.format[AwayTeam]
  implicit val homeTeamFormat: Format[HomeTeam] = Json.format[HomeTeam]
  implicit val matchGameFormat: Format[Match] = Json.format[Match]

}
