package models

import java.util.UUID

import play.api.libs.json.{JsPath, Json, OFormat, Reads, _}

case class Campaign(
                     elementType: String,
                     id: String,
                     name: String,
                     description: Option[String],
                     content: List[WorldElement]
                   ) extends WorldElement {

  def updateContent(replacement: List[WorldElement]): WorldElement = this.copy(content = replacement)

}

object Campaign {
  val reads: Reads[Campaign] = Json.reads[Campaign]
  val writes: OWrites[Campaign] = Json.writes[Campaign]

  def create(name: String, description: Option[String]): Campaign = {
    Campaign("campaign", UUID.randomUUID().toString, name, description, List.empty[WorldElement])
  }
}

case class Plane(
                  elementType: String,
                  id: String,
                  name: String,
                  description: Option[String],
                  content: List[WorldElement],
                  alignment: String
                ) extends WorldElement {

  def updateContent(replacement: List[WorldElement]): WorldElement = this.copy(content = replacement)

}

object Plane {
  val reads: Reads[Plane] = Json.reads[Plane]
  val writes: OWrites[Plane] = Json.writes[Plane]

  def create(name: String, description: Option[String], alignment: String): Plane = {
    Plane("plane", UUID.randomUUID().toString, name, description, List.empty[WorldElement], alignment)
  }
}

case class Land(
                 elementType: String,
                 id: String,
                 name: String,
                 description: Option[String],
                 content: List[WorldElement]
               ) extends WorldElement {

  def updateContent(replacement: List[WorldElement]): WorldElement = this.copy(content = replacement)

}

object Land {
  val reads: Reads[Land] = Json.reads[Land]
  val writes: OWrites[Land] = Json.writes[Land]

  def create(name: String, description: Option[String]): Land = {
    Land("land", UUID.randomUUID().toString, name, description, List.empty[WorldElement])
  }
}

sealed trait WorldElement {

  val elementType: String
  val id: String
  val name: String
  val description: Option[String]
  val content: List[WorldElement]

  def replace(elementId: String, element: WorldElement): WorldElement = {
    if (elementId == id) element else this.updateContent(replacement = content.map(_.replace(elementId, element)))
  }

  def addElementTo(elementId: String, element: WorldElement): WorldElement = {
    if (elementId == id) {
      this.updateContent(replacement = content :+ element)
    } else {
      this.updateContent(replacement = content.map(_.addElementTo(elementId, element)))
    }
  }

  def removeElementId(elementId: String): WorldElement = {
    this.updateContent(replacement = content.filterNot(_.id == elementId).map(_.removeElementId(elementId)))
  }

  def find(elementId: String): Option[WorldElement] = {
    if (id == elementId) Some(this) else content.flatMap(_.find(elementId)).headOption
  }

  def updateContent(replacement: List[WorldElement]): WorldElement

}

object WorldElement {

  val jsonToWorldElement: JsValue => JsResult[WorldElement] = { json =>
    (json \ "elementType").validateOpt[String] flatMap {
      case Some("campaign") => Json.fromJson(json)(Campaign.reads)
      case Some("plane") => Json.fromJson(json)(Plane.reads)
      case Some("land") => Json.fromJson(json)(Land.reads)
      case _ => JsError(JsPath \ "elementType", "error.invalid")
    }
  }

  val worldElementToJson: WorldElement => JsObject = {
    case campaign: Campaign => Json.toJsObject(campaign)(Campaign.writes)
    case plane: Plane => Json.toJsObject(plane)(Plane.writes)
    case land: Land => Json.toJsObject(land)(Land.writes)
  }

  val reads: Reads[WorldElement] = Reads[WorldElement](jsonToWorldElement)
  val writes: OWrites[WorldElement] = OWrites[WorldElement](worldElementToJson)

  implicit val format: OFormat[WorldElement] = OFormat(reads, writes)

}
