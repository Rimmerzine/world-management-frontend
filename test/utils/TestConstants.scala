package utils

import models.Campaign
import play.api.libs.json.{JsObject, JsValue, Json}

trait TestConstants extends CampaignConstants {

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
