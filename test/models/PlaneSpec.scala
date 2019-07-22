package models

import helpers.UnitSpec
import play.api.libs.json._
import utils.TestConstants

class PlaneSpec extends UnitSpec with TestConstants {

  "fromJson" must {
    "read from json successfully" when {
      "description is defined" in {
        Json.fromJson[Plane](testPlaneJson) mustBe JsSuccess(testPlane)
      }
      "description is not defined" in {
        Json.fromJson[Plane](testPlaneMinimalJson) mustBe JsSuccess(testPlaneMinimal)
      }
    }
    "fail to read from json" when {
      "campaignId is missing" in {
        val json: JsObject = Json.obj(
          "planeId" -> testPlaneId,
          "name" -> testPlaneName,
          "description" -> testPlaneDescription,
          "alignment" -> testPlaneAlignment
        )
        Json.fromJson[Plane](json) mustBe JsError(JsPath \ "campaignId", "error.path.missing")
      }
      "planeId is missing" in {
        val json: JsObject = Json.obj(
          "campaignId" -> testCampaignId,
          "name" -> testPlaneName,
          "description" -> testPlaneDescription,
          "alignment" -> testPlaneAlignment
        )
        Json.fromJson[Plane](json) mustBe JsError(JsPath \ "planeId", "error.path.missing")
      }
      "name is missing" in {
        val json: JsObject = Json.obj(
          "campaignId" -> testCampaignId,
          "planeId" -> testPlaneId,
          "description" -> testPlaneDescription,
          "alignment" -> testPlaneAlignment
        )
        Json.fromJson[Plane](json) mustBe JsError(JsPath \ "name", "error.path.missing")
      }
      "alignment is missing" in {
        val json: JsObject = Json.obj(
          "campaignId" -> testCampaignId,
          "planeId" -> testPlaneId,
          "name" -> testPlaneName,
          "description" -> testPlaneDescription
        )
        Json.fromJson[Plane](json) mustBe JsError(JsPath \ "alignment", "error.path.missing")
      }
    }
  }

  "toJson" must {
    "write to json successfully" when {
      "description is defined" in {
        Json.toJson(testPlane) mustBe testPlaneJson
      }
      "description is not defined" in {
        Json.toJson(testPlaneMinimal) mustBe testPlaneMinimalJson
      }
    }
  }

  "create" must {
    "create a new model with a random id" in {
      val plane: Plane = Plane.create(testCampaignId, testPlaneName, Some(testPlaneDescription), testPlaneAlignment)

      plane.campaignId mustBe testCampaignId
      plane.name mustBe testPlaneName
      plane.description mustBe Some(testPlaneDescription)
      plane.alignment mustBe testPlaneAlignment
    }
  }

}
