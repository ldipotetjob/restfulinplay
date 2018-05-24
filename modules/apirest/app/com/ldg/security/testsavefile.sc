import scala.io.Source

def getStoredToken(file: String): Seq[String] = {
  val csvMatchs = Source.fromFile(file)
  csvMatchs.getLines().toSeq
}

getStoredToken("fakedb.txt")


//sdf.parse(res24)

import java.io.File
import java.io.PrintWriter

import scala.io.Source

object Write {
  def main(args: Array[String]) {
    import java.io._
    val pw = new PrintWriter(new File("hello.txt" ))
    pw.write("Hello, world")
    pw.close
  }

}
Write.main(Array.empty[String])