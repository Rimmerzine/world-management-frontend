package models

import java.util.UUID

import play.api.libs.json.{Json, OFormat}

case class Campaign(id: String, name: String, description: Option[String])

object Campaign {

  def create(name: String, description: Option[String]): Campaign = Campaign(UUID.randomUUID().toString, name, description)

  implicit val format: OFormat[Campaign] = Json.format[Campaign]

}
