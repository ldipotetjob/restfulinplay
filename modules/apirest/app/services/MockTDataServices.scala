package services

import com.ldg.model.{Match, TestMatchObject}

class MockTDataServices extends TDataServices{
  def modelOfMatchFootball(file: String): Seq[Match] = Seq(TestMatchObject.matchGame)
}
