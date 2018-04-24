package services

import com.ldg.model.Match

/** Simulate a Service*/
trait TDataServices {
  def modelOfMatchPremier(file: String): Seq[Match]
}
