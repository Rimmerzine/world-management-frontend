package models

import helpers.UnitSpec
import play.api.libs.json._
import utils.TestConstants

class CampaignSpec extends UnitSpec with TestConstants {

  "fromJson" must {
    "read from json successfully" when {
      "description is defined" in {
        Json.fromJson[Campaign](testCampaignJson) mustBe JsSuccess(testCampaign)
      }
      "description is not defined" in {
        Json.fromJson[Campaign](testCampaignMinimalJson) mustBe JsSuccess(testCampaignMinimal)
      }
    }
    "fail to read from json" when {
      "id is missing" in {
        val json: JsObject = Json.obj("name" -> testCampaignName, "description" -> Some(testCampaignDescription))
        Json.fromJson[Campaign](json) mustBe JsError(JsPath \ "id", "error.path.missing")
      }
      "name is missing" in {
        val json: JsObject = Json.obj("id" -> testCampaignId, "description" -> Some(testCampaignDescription))
        Json.fromJson[Campaign](json) mustBe JsError(JsPath \ "name", "error.path.missing")
      }
    }
  }

  "toJson" must {
    "write to json successfully" when {
      "description is defined" in {
        Json.toJson(testCampaign) mustBe testCampaignJson
      }
      "description is not defined" in {
        Json.toJson(testCampaignMinimal) mustBe testCampaignMinimalJson
      }
    }
  }

  "create" must {
    "create a new model with a random id" in {
      val campaign: Campaign = Campaign.create(testCampaignName, Some(testCampaignDescription))

      campaign.name mustBe testCampaignName
      campaign.description mustBe Some(testCampaignDescription)
    }
  }

}
