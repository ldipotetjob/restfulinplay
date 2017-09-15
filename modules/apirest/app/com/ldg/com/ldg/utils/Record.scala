package com.ldg.utils

/**
  * Created by ldipotet on 23/7/17.
  */
case class Record(val foo: String, val bar: String)

object Record {
  def sampleRecords = List(Record("f1","b1"), Record("f2","b2"), Record("f3","b3"))
}