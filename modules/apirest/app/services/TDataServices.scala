package services

import com.ldg.model.Match

/** Simulate a Service*/
trait TDataServices {
  def modelOfMatchFootball(file: String): Seq[Match]
}
