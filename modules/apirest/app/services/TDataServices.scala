package services

import com.ldg.model.Match

trait TDataServices {

  def modelOfMatchPremier(file:String):Seq[Match]

}
