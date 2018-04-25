package services

import com.ldg.model.{Match, TestMatchObject}

class MockTDataServices extends TDataServices{
  def modelOfMatchPremier(file: String): Seq[Match] = Seq(TestMatchObject.matchGame)
}
