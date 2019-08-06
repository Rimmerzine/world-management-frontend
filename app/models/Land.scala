package models

import java.util.UUID

import play.api.libs.json.{Json, OFormat}

case class Land(planeId: String, landId: String, name: String, description: Option[String])

object Land {

  def create(planeId: String, name: String, description: Option[String]): Land = Land(planeId, UUID.randomUUID().toString, name, description)

  implicit val format: OFormat[Land] = Json.format[Land]

}
