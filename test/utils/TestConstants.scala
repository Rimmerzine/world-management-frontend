package utils

import models.{Campaign, Land, Plane, WorldElement}
import play.api.libs.json.{JsObject, Json}
import scalatags.Text
import scalatags.Text.all.html

trait TestConstants {

  val emptyJson: JsObject = Json.obj()
  val emptyHtmlTag: Text.TypedTag[String] = html()

  val campaignId: String = "testCampaignId"
  val campaignName: String = "testCampaignName"
  val campaignDescription: String = "testCampaignDescription"
  val campaign: Campaign = Campaign("campaign", campaignId, campaignName, Some(campaignDescription), List.empty[WorldElement])

  val campaignJson: JsObject = Json.obj(
    "elementType" -> "campaign",
    "id" -> campaignId,
    "name" -> campaignName,
    "description" -> campaignDescription,
    "content" -> Json.arr()
  )

  val planeId: String = "testPlaneId"
  val planeName: String = "testPlaneName"
  val planeDescription: String = "testPlaneDescription"
  val planeAlignment: String = "lawful-good"
  val plane: Plane = Plane("plane", planeId, planeName, Some(planeDescription), List.empty[WorldElement], planeAlignment)
  val planeJson: JsObject = Json.obj(
    "elementType" -> "plane",
    "id" -> planeId,
    "name" -> planeName,
    "description" -> planeDescription,
    "content" -> Json.arr(),
    "alignment" -> planeAlignment
  )

  val landId: String = "testLandId"
  val landName: String = "testLandName"
  val landDescription: String = "testLandDescription"
  val land: Land = Land("land", landId, landName, Some(landDescription), List.empty[WorldElement])
  val landJson: JsObject = Json.obj(
    "elementType" -> "land",
    "id" -> landId,
    "name" -> landName,
    "description" -> landDescription,
    "content" -> Json.arr()
  )

  val campaignWithContent: Campaign = campaign.copy(content = List(plane))

}
