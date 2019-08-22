package connectors

import helpers.UnitSpec
import mockws.{MockWS, MockWSHelpers}
import models.Campaign
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import utils.ErrorModel.{CampaignNotFound, JsonParseError, UnexpectedStatus}
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CampaignConnectorSpec extends UnitSpec with MockWSHelpers with TestConstants {

  val testBaseUrl: String = "testBaseUrl"

  class Setup(url: String, verb: String, result: Result) {

    val mockWS: MockWS = MockWS {
      case (`verb`, `url`) => Action.async {
        Future.successful(result)
      }
    }

    lazy val connector: CampaignConnector = new CampaignConnector {
      val wsClient: WSClient = mockWS
      val baseUrl: String = testBaseUrl
    }

  }

  "retrieveAllCampaigns" must {
    "return a list of campaigns" when {
      "Ok is returned with campaign json" in new Setup(s"$testBaseUrl/campaigns/retrieve", GET, Ok(Json.arr(campaignJson, campaignJson))) {
        await(connector.retrieveAllCampaigns) mustBe Right(List(campaign, campaign))
      }
      "NoContent is returned" in new Setup(s"$testBaseUrl/campaigns/retrieve", GET, NoContent) {
        await(connector.retrieveAllCampaigns) mustBe Right(List.empty[Campaign])
      }
    }
    "return a JsonParseError" when {
      "Ok is returned but could not be parsed into a list of campaigns" in new Setup(s"$testBaseUrl/campaigns/retrieve", GET, Ok(emptyJson)) {
        await(connector.retrieveAllCampaigns) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaigns/retrieve", GET, InternalServerError) {
        await(connector.retrieveAllCampaigns) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "retrieveSingleCampaign" must {
    "return a campaign" when {
      "Ok is returned with a campaign json" in new Setup(s"$testBaseUrl/campaigns/retrieve/${campaign.id}", GET, Ok(campaignJson)) {
        await(connector.retrieveSingleCampaign(campaign.id)) mustBe Right(campaign)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a campaign" in new Setup(s"$testBaseUrl/campaigns/retrieve/${campaign.id}", GET, Ok(emptyJson)) {
        await(connector.retrieveSingleCampaign(campaign.id)) mustBe Left(JsonParseError)
      }
    }
    "return CampaignNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/campaigns/retrieve/${campaign.id}", GET, NotFound) {
        await(connector.retrieveSingleCampaign(campaign.id)) mustBe Left(CampaignNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaigns/retrieve/${campaign.id}", GET, InternalServerError) {
        await(connector.retrieveSingleCampaign(campaign.id)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "createCampaign" must {
    "return a campaign" when {
      "Created is returned with a campaign json" in new Setup(s"$testBaseUrl/campaigns/create", POST, Created(campaignJson)) {
        await(connector.createCampaign(campaign)) mustBe Right(campaign)
      }
    }
    "return JsonParseError" when {
      "Created is returned but could not be parsed into a campaign" in new Setup(s"$testBaseUrl/campaigns/create", POST, Created(emptyJson)) {
        await(connector.createCampaign(campaign)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaigns/create", POST, InternalServerError) {
        await(connector.createCampaign(campaign)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "updateCampaign" must {
    "return a campaign" when {
      "Ok is returned with a campaign json" in new Setup(s"$testBaseUrl/campaigns/update", PUT, Ok(campaignJson)) {
        await(connector.updateCampaign(campaign)) mustBe Right(campaign)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a campaign" in new Setup(s"$testBaseUrl/campaigns/update", PUT, Ok(emptyJson)) {
        await(connector.updateCampaign(campaign)) mustBe Left(JsonParseError)
      }
    }
    "return CampaignNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/campaigns/update", PUT, NotFound) {
        await(connector.updateCampaign(campaign)) mustBe Left(CampaignNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaigns/update", PUT, InternalServerError) {
        await(connector.updateCampaign(campaign)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "removeCampaign" must {
    "return a campaign" when {
      "Ok is returned with a campaign json" in new Setup(s"$testBaseUrl/campaigns/remove/${campaign.id}", DELETE, Ok(campaignJson)) {
        await(connector.removeCampaign(campaign.id)) mustBe Right(campaign)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a campaign" in new Setup(s"$testBaseUrl/campaigns/remove/${campaign.id}", DELETE, Ok(emptyJson)) {
        await(connector.removeCampaign(campaign.id)) mustBe Left(JsonParseError)
      }
    }
    "return CampaignNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/campaigns/remove/${campaign.id}", DELETE, NotFound) {
        await(connector.removeCampaign(campaign.id)) mustBe Left(CampaignNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/campaigns/remove/${campaign.id}", DELETE, InternalServerError) {
        await(connector.removeCampaign(campaign.id)) mustBe Left(UnexpectedStatus)
      }
    }
  }

}
