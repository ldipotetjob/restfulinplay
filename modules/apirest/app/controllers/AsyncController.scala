/**
  * Created by ldipotet on 13/7/17.
  */

package controllers

import akka.actor.ActorSystem

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._


import com.ldg.implicitconversions.{Resident, Location, Place}
import com.ldg.implicitconversions.MyOwnConversions._


/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
@Singleton
class AsyncController @Inject() (actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  /**
    * Extra Code for test some simple actions
    */

  val jsonParse: JsValue = Json.parse("""
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
                                      """)

  val jsonObj: JsValue = JsObject(Seq(
    "name" -> JsString("Watership Down"),
    "location" -> JsObject(Seq("lat" -> JsNumber(51.235685), "long" -> JsNumber(-1.309197))),
    "residents" -> JsArray(Seq(
      JsObject(Seq(
        "name" -> JsString("Fiver"),
        "age" -> JsNumber(4),
        "role" -> JsNull
      )),
      JsObject(Seq(
        "name" -> JsString("Bigwig"),
        "age" -> JsNumber(6),
        "role" -> JsString("Owsla")
      ))
    ))
  ))

  val json: JsValue = Json.obj(
    "name" -> "Watership Down",
    "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197),
    "residents" -> Json.arr(
      Json.obj(
        "name" -> "Fiver",
        "age" -> 4,
        "role" -> JsNull
      ),
      Json.obj(
        "name" -> "Bigwig",
        "age" -> 6,
        "role" -> "Owsla"
      )
    )
  )

  //get => exists a getOrElse(v=> JSvalue)
  val nametmp:String = jsonParse.\("name").get.toString()
  val nametmp1:String = jsonParse.toString()

  val jsonString = Json.toJson("Fiver")

  val place = Place(
    "Watership Down",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Fiver", 4, None),
      Resident("Bigwig", 6, Some("Owsla"))
    )
  )


  /**
    * Create an Action that returns a plain text message after a delay
    * of 1 second.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/message`.
    *
    * All action are execute in async way so Action.async is Just because the code that
    * it execute return a future value.
    */

  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  /**
    *
    * @param delayTime
    * @return a string with a json representation (jsonend)
    */

  val jsonend:JsValue = Json.toJson(place)

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) { promise.success(Json.stringify(jsonend)) }
    promise.future
  }

  /**
    * It's just a Simple action tha receive a parameter
   */
  def custommessage(message:String) = Action {
     request=> Ok(s"Good morning:${message}")
  }

}
