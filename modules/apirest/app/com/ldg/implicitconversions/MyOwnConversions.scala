package com.ldg.implicitconversions

/**
  * This are the necessary conversion for execute all examples that appears in
  * the official play documentation
  *
  * Created by ldipotet on 20/6/17.
  */
import play.api.libs.functional.syntax._
import play.api.libs.json._

object MyOwnConversions {

  implicit val residentWrites = new Writes[Resident] {
    def writes(resident: Resident) = Json.obj(
      "name" -> resident.name,
      "age" -> resident.age,
      "role" -> resident.role
    )
  }

  // functional_syntax solve the "and" in the following line of code

  implicit val residentReads: Reads[Resident] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "role").readNullable[String]
    )(Resident.apply _)


/****

The following READ/WRITE have have been substituted for the next line of code
after this part of commented code

implicit val locationWrites = new Writes[Location] {
    def writes(location: Location) = Json.obj(
      "lat" -> location.lat,
      "long" -> location.long
    )
  }

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
      (JsPath \ "long").read[Double]
    )(Location.apply _)


  ****/

  //this Format could be interesting in case of make any needed validations
  //of an specific field of the JSON
  implicit val locationFormat: Format[Location] = (
    (JsPath \ "lat").format[Double] and
      (JsPath \ "long").format[Double]
    )(Location.apply, unlift(Location.unapply))

/****

    The following READ/WRITE have have been substituted for the next line of code
    after this part of commented code (implicit val placeFormat:Format[Place] = Json.format[Place])

      implicit val placeReads: Reads[Place] = (
            (JsPath \ "name").read[String] and
              (JsPath \ "location").read[Location] and
              (JsPath \ "residents").read[Seq[Resident]]
            )(Place.apply _)

      implicit val placeWrites = new Writes[Place] {
        def writes(place: Place) = Json.obj(
          "name" -> place.name,
          "location" -> place.location,
          "residents" -> place.residents)
      }

****/


implicit val placeFormat: Format[Place] = Json.format[Place]

}

/*********************************************************************
  *  The following case class are necessary for test the example en
  *  posted in play documentacion
  *
  * *******************************************************************
  */
case class Location(lat: Double, long: Double)
case class Resident(name: String, age: Int, role: Option[String])
case class Place(name: String, location: Location, residents: Seq[Resident])
