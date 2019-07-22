package utils

import models.{Campaign, Plane}
import play.api.libs.json.{JsObject, JsValue, Json}

trait TestConstants extends CampaignConstants with PlaneConstants {

  val emptyJson: JsObject = Json.obj()
  val emptyHtml: String = "<html></html>"

}

trait CampaignConstants {

  val testCampaignId: String = "testCampaignId"
  val testCampaignName: String = "testCampaignName"
  val testCampaignDescription: String = "testCampaignDescription"

  val testCampaign: Campaign = Campaign(testCampaignId, testCampaignName, Some(testCampaignDescription))
  val testCampaignMinimal: Campaign = Campaign(testCampaignId + "Min", testCampaignName, None)

  def testCampaigns(count: Int): List[Campaign] = {
    for {
      num <- (0 to count).toList
    } yield {
      Campaign(testCampaignId + num, testCampaignName + num, Some(testCampaignDescription + num))
    }
  }

  def testCampaignsJson(count: Int): JsValue = Json.toJson(testCampaigns(count))

  val testCampaignJson: JsObject = Json.obj("id" -> testCampaignId, "name" -> testCampaignName, "description" -> testCampaignDescription)
  val testCampaignMinimalJson: JsObject = Json.obj("id" -> (testCampaignId + "Min"), "name" -> testCampaignName)

}

trait PlaneConstants {

  val testCampaignId: String

  val testPlaneId: String = "testPlaneId"
  val testPlaneName: String = "testPlaneName"
  val testPlaneDescription: String = "testPlaneDescription"
  val testPlaneAlignment: String = "unaligned"

  val testPlane: Plane = Plane(testCampaignId, testPlaneId, testPlaneName, Some(testPlaneDescription), testPlaneAlignment)
  val testPlaneMinimal: Plane = Plane(testCampaignId, testPlaneId + "Min", testPlaneName, None, testPlaneAlignment)

  def testPlanes(count: Int): List[Plane] = {
    for {
      num <- (0 to count).toList
    } yield {
      Plane(testCampaignId, testPlaneId + num, testPlaneName + num, Some(testPlaneDescription + num), testPlaneAlignment + num)
    }
  }

  def testPlanesJson(count: Int): JsValue = Json.toJson(testPlanes(count))

  val testPlaneJson: JsObject = Json.obj(
    "campaignId" -> testCampaignId,
    "planeId" -> testPlaneId,
    "name" -> testPlaneName,
    "description" -> testPlaneDescription,
    "alignment" -> testPlaneAlignment
  )

  val testPlaneMinimalJson: JsObject = Json.obj(
    "campaignId" -> testCampaignId,
    "planeId" -> (testPlaneId + "Min"),
    "name" -> testPlaneName,
    "alignment" -> testPlaneAlignment
  )

}
