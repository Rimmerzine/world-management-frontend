package connectors

import helpers.UnitSpec
import mockws.{MockWS, MockWSHelpers}
import models.Plane
import play.api.libs.ws.WSClient
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import utils.ErrorModel.{JsonParseError, PlaneNotFound, UnexpectedStatus}
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PlaneConnectorSpec extends UnitSpec with MockWSHelpers with TestConstants {

  val testBaseUrl: String = "testBaseUrl"

  class Setup(url: String, verb: String, result: Result) {

    val mockWS: MockWS = MockWS {
      case (`verb`, `url`) => Action.async {
        Future.successful(result)
      }
    }

    lazy val connector: PlaneConnector = new PlaneConnector {
      val wsClient: WSClient = mockWS
      val baseUrl: String = testBaseUrl
    }

  }

  "retrieveAllPlanes" must {
    "return a list of planes" when {
      "Ok is returned with campaign json" in new Setup(s"$testBaseUrl/campaign/$testCampaignId/planes", GET, Ok(testPlanesJson(2))) {
        await(connector.retrieveAllPlanes(testCampaignId)) mustBe Right(testPlanes(2))
      }
      "NoContent is returned" in new Setup(s"$testBaseUrl/campaign/$testCampaignId/planes", GET, NoContent) {
        await(connector.retrieveAllPlanes(testCampaignId)) mustBe Right(List.empty[Plane])
      }
    }
    "return a JsonParseError" when {
      "Ok is returned but could not be parsed into a list of planes" in new Setup(s"$testBaseUrl/campaign/$testCampaignId/planes", GET, Ok(emptyJson)) {
        await(connector.retrieveAllPlanes(testCampaignId)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaign/$testCampaignId/planes", GET, InternalServerError) {
        await(connector.retrieveAllPlanes(testCampaignId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "retrieveSinglePlane" must {
    "return a plane" when {
      "Ok is returned with a plane json" in new Setup(s"$testBaseUrl/planes/retrieve/${testPlane.planeId}", GET, Ok(testPlaneJson)) {
        await(connector.retrieveSinglePlane(testPlane.planeId)) mustBe Right(testPlane)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a plane" in new Setup(s"$testBaseUrl/planes/retrieve/${testPlane.planeId}", GET, Ok(emptyJson)) {
        await(connector.retrieveSinglePlane(testPlane.planeId)) mustBe Left(JsonParseError)
      }
    }
    "return PlaneNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/planes/retrieve/${testPlane.planeId}", GET, NotFound) {
        await(connector.retrieveSinglePlane(testPlane.planeId)) mustBe Left(PlaneNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/planes/retrieve/${testPlane.planeId}", GET, InternalServerError) {
        await(connector.retrieveSinglePlane(testPlane.planeId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "createPlane" must {
    "return a plane" when {
      "Created is returned with a plane json" in new Setup(s"$testBaseUrl/planes/create", POST, Created(testPlaneJson)) {
        await(connector.createPlane(testPlane)) mustBe Right(testPlane)
      }
    }
    "return JsonParseError" when {
      "Created is returned but could not be parsed into a plane" in new Setup(s"$testBaseUrl/planes/create", POST, Created(emptyJson)) {
        await(connector.createPlane(testPlane)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/planes/create", POST, InternalServerError) {
        await(connector.createPlane(testPlane)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "updatePlane" must {
    "return a plane" when {
      "Ok is returned with a plane json" in new Setup(s"$testBaseUrl/planes/update", PUT, Ok(testPlaneJson)) {
        await(connector.updatePlane(testPlane)) mustBe Right(testPlane)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a plane" in new Setup(s"$testBaseUrl/planes/update", PUT, Ok(emptyJson)) {
        await(connector.updatePlane(testPlane)) mustBe Left(JsonParseError)
      }
    }
    "return PlaneNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/planes/update", PUT, NotFound) {
        await(connector.updatePlane(testPlane)) mustBe Left(PlaneNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/planes/update", PUT, InternalServerError) {
        await(connector.updatePlane(testPlane)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "removePlane" must {
    "return a plane" when {
      "Ok is returned with a plane json" in new Setup(s"$testBaseUrl/planes/remove/${testPlane.planeId}", DELETE, Ok(testPlaneJson)) {
        await(connector.removePlane(testPlane.planeId)) mustBe Right(testPlane)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a plane" in new Setup(s"$testBaseUrl/planes/remove/${testPlane.planeId}", DELETE, Ok(emptyJson)) {
        await(connector.removePlane(testPlane.planeId)) mustBe Left(JsonParseError)
      }
    }
    "return PlaneNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/planes/remove/${testPlane.planeId}", DELETE, NotFound) {
        await(connector.removePlane(testPlane.planeId)) mustBe Left(PlaneNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/planes/remove/${testPlane.planeId}", DELETE, InternalServerError) {
        await(connector.removePlane(testPlane.planeId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

}
