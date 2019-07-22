package models

import java.util.UUID

import play.api.libs.json.{Json, OFormat}

case class Plane(campaignId: String, planeId: String, name: String, description: Option[String], alignment: String)

object Plane {

  def create(campaignId: String, name: String, description: Option[String], alignment: String): Plane = {
    Plane(campaignId, UUID.randomUUID().toString, name, description, alignment)
  }

  implicit val format: OFormat[Plane] = Json.format[Plane]

}
