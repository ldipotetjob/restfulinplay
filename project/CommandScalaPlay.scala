import sbt._

object CommandScalaPlay {


/** https://www.scala-sbt.org/sbt-native-packager/gettingstarted.html#native-tools */
  // A simple, multiple-argument command that prints "Hi" followed by the arguments.
  // Again, it leaves the current state unchanged.
  // launching from console a production building:  sbt -Denv=prod buildAll
  def buildAll = Command.args("buildAll", "<name>") { (state, args) =>
    println("buildAll command generating tgz in Universal" + args.mkString(" "))
    "reload"::"clean"::"compile"::"universal:packageZipTarball"::
    state
  }
}