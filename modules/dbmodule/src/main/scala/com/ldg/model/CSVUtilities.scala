/**
  * Created by ldipotet on 29/6/17.
  */

package com.ldg.model

import java.io._
import scala.io.Source

/**
  * Created by ldipotet on 6/7/17.
  * for testing regex: https://regex101.com
  *
  * for testing utilities
  * resources/regex.png
  *
  */

object CSVUtilities {

  /**
    * Process a block of code and close the io implied
    *
    */
  def processCloseable[T <: Closeable, R](resource: T)(block: T => R): R = {
    try { block(resource) }
    finally { resource.close() }
  }

  def main(args: Array[String]): Unit = {

    //look file INTO resources folder
    val source = Source.fromURL(getClass.getResource("/football.csv"))

    val lines = source.getLines

    /*
      Structure to convert ==>

    0 - matches.matchNunmber
    1 - matches.awayTeam.goals
    2 - listplayerToStr(matches.awayTeam.goalsPlayer)
    3 - matches.awayTeam.name
    4 - matches.date
    5 - matches.homeTeam.goals
    6 - listplayerToStr(matches.homeTeam.goalsPlayer)
    7 - matches.homeTeam.name
    8 - matches.season
    */

    val newCsvMap:Seq[Any] = lines.map(x=>{
      val arrstr= x.split("(,)(?=(?:[^}]|\\{[^{]*})*$)")
      //we can NOT use String Interpolation so we to save it to variables
      println(arrstr(2).length)
      val (awayGoals,lisPlayerAway,awayTeamName,date,homeGoals,listPlayerHome,homeTeanName,season) =
        (arrstr(1),arrstr(2),arrstr(3),arrstr(4),arrstr(5),arrstr(6),arrstr(7),arrstr(8))
        s"$awayGoals,$lisPlayerAway,$awayTeamName,$date,$homeGoals,$listPlayerHome,$homeTeanName,$season"
    }).toSeq

    processCloseable(new PrintWriter(new File("football.txt")) ){
      writer =>
        newCsvMap.foreach{ x=>
          writer.write(x +  "\n")
        }
    }
    source.close
  }

}
