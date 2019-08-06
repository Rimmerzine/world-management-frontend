package connectors

import helpers.UnitSpec
import mockws.{MockWS, MockWSHelpers}
import models.Land
import play.api.libs.ws.WSClient
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import utils.ErrorModel.{JsonParseError, LandNotFound, UnexpectedStatus}
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LandConnectorSpec extends UnitSpec with MockWSHelpers with TestConstants {

  val testBaseUrl: String = "testBaseUrl"

  class Setup(url: String, verb: String, result: Result) {

    val mockWS: MockWS = MockWS {
      case (`verb`, `url`) => Action.async {
        Future.successful(result)
      }
    }

    lazy val connector: LandConnector = new LandConnector {
      val wsClient: WSClient = mockWS
      val baseUrl: String = testBaseUrl
    }

  }

  "retrieveAllLands" must {
    "return a list of lands" when {
      "Ok is returned with land json" in new Setup(s"$testBaseUrl/plane/$testPlaneId/lands", GET, Ok(testLandsJson(2))) {
        await(connector.retrieveAllLands(testPlaneId)) mustBe Right(testLands(2))
      }
      "NoContent is returned" in new Setup(s"$testBaseUrl/plane/$testPlaneId/lands", GET, NoContent) {
        await(connector.retrieveAllLands(testPlaneId)) mustBe Right(List.empty[Land])
      }
    }
    "return a JsonParseError" when {
      "Ok is returned but could not be parsed into a list of lands" in new Setup(s"$testBaseUrl/plane/$testPlaneId/lands", GET, Ok(emptyJson)) {
        await(connector.retrieveAllLands(testPlaneId)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/plane/$testPlaneId/lands", GET, InternalServerError) {
        await(connector.retrieveAllLands(testPlaneId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "retrieveSingleLand" must {
    "return a land" when {
      "Ok is returned with a land json" in new Setup(s"$testBaseUrl/lands/retrieve/${testLand.landId}", GET, Ok(testLandJson)) {
        await(connector.retrieveSingleLand(testLand.landId)) mustBe Right(testLand)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a land" in new Setup(s"$testBaseUrl/lands/retrieve/${testLand.landId}", GET, Ok(emptyJson)) {
        await(connector.retrieveSingleLand(testLand.landId)) mustBe Left(JsonParseError)
      }
    }
    "return LandNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/lands/retrieve/${testLand.landId}", GET, NotFound) {
        await(connector.retrieveSingleLand(testLand.landId)) mustBe Left(LandNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/lands/retrieve/${testLand.landId}", GET, InternalServerError) {
        await(connector.retrieveSingleLand(testLand.landId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "createLand" must {
    "return a land" when {
      "Created is returned with a land json" in new Setup(s"$testBaseUrl/lands/create", POST, Created(testLandJson)) {
        await(connector.createLand(testLand)) mustBe Right(testLand)
      }
    }
    "return JsonParseError" when {
      "Created is returned but could not be parsed into a land" in new Setup(s"$testBaseUrl/lands/create", POST, Created(emptyJson)) {
        await(connector.createLand(testLand)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/lands/create", POST, InternalServerError) {
        await(connector.createLand(testLand)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "updateLand" must {
    "return a land" when {
      "Ok is returned with a land json" in new Setup(s"$testBaseUrl/lands/update", PUT, Ok(testLandJson)) {
        await(connector.updateLand(testLand)) mustBe Right(testLand)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a land" in new Setup(s"$testBaseUrl/lands/update", PUT, Ok(emptyJson)) {
        await(connector.updateLand(testLand)) mustBe Left(JsonParseError)
      }
    }
    "return LandNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/lands/update", PUT, NotFound) {
        await(connector.updateLand(testLand)) mustBe Left(LandNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/lands/update", PUT, InternalServerError) {
        await(connector.updateLand(testLand)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "removeLand" must {
    "return a land" when {
      "Ok is returned with a land json" in new Setup(s"$testBaseUrl/lands/remove/${testLand.landId}", DELETE, Ok(testLandJson)) {
        await(connector.removeLand(testLand.landId)) mustBe Right(testLand)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a land" in new Setup(s"$testBaseUrl/lands/remove/${testLand.landId}", DELETE, Ok(emptyJson)) {
        await(connector.removeLand(testLand.landId)) mustBe Left(JsonParseError)
      }
    }
    "return LandNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/lands/remove/${testLand.landId}", DELETE, NotFound) {
        await(connector.removeLand(testLand.landId)) mustBe Left(LandNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/lands/remove/${testLand.landId}", DELETE, InternalServerError) {
        await(connector.removeLand(testLand.landId)) mustBe Left(UnexpectedStatus)
      }
    }
  }

}
