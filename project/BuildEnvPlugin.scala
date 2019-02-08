import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * make call in this way for generate development distro
  * sbt -Denv=dev reload clean compile stage
  *
  * make call in this way for generate production distro
  * sbt -Denv=prod reload clean compile stage
  *
  */

/** sets the build environment */
object BuildEnvPlugin extends AutoPlugin {

  // make sure it triggers automatically
  override def trigger = AllRequirements
  override def requires = JvmPlugin

  object autoImport {
    object BuildEnv extends Enumeration {
      val Production, Stage, Test, Development = Value
    }
    val buildEnv = settingKey[BuildEnv.Value]("the current build environment")
  }
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    buildEnv := {
      sys.props.get("env")
        .orElse(sys.env.get("BUILD_ENV"))
        .flatMap {
          case "prod" => Some(BuildEnv.Production)
          case "stage" => Some(BuildEnv.Stage)
          case "test" => Some(BuildEnv.Test)
          case "dev" => Some(BuildEnv.Development)
          case unkown => None
        }
        .getOrElse(BuildEnv.Development)
    },
    // message indicating in what environment you are building on
    onLoadMessage := {
      val defaultMessage = onLoadMessage.value
      val env = buildEnv.value
      s"""|$defaultMessage
          |Running in build environment: $env""".stripMargin
    }
  )
}
