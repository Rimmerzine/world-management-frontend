package models

import helpers.UnitSpec
import play.api.libs.json._
import utils.TestConstants

class LandSpec extends UnitSpec with TestConstants {

  "fromJson" must {
    "read from json successfully" when {
      "description is defined" in {
        Json.fromJson[Land](testLandJson) mustBe JsSuccess(testLand)
      }
      "description is not defined" in {
        Json.fromJson[Land](testLandMinimalJson) mustBe JsSuccess(testLandMinimal)
      }
    }
    "fail to read from json" when {
      "planeId is missing" in {
        val json: JsObject = testLandJson - "planeId"
        Json.fromJson[Land](json) mustBe JsError(JsPath \ "planeId", "error.path.missing")
      }
      "landId is missing" in {
        val json: JsObject = testLandJson - "landId"
        Json.fromJson[Land](json) mustBe JsError(JsPath \ "landId", "error.path.missing")
      }
      "name is missing" in {
        val json: JsObject = testLandJson - "name"
        Json.fromJson[Land](json) mustBe JsError(JsPath \ "name", "error.path.missing")
      }
    }
  }

  "toJson" must {
    "write to json" when {
      "description is defined" in {
        Json.toJson(testLand) mustBe testLandJson
      }
      "description is not defined" in {
        Json.toJson(testLandMinimal) mustBe testLandMinimalJson
      }
    }
  }

  "create" must {
    "create a new model with a random id" in {
      val land: Land = Land.create(testPlaneId, testLandName, Some(testLandDescription))

      land.planeId mustBe testPlaneId
      land.name mustBe testLandName
      land.description mustBe Some(testLandDescription)
    }
  }

}
