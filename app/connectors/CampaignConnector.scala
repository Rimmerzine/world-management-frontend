package connectors

import config.AppConfig
import javax.inject.Inject
import models.Campaign
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import utils.ErrorModel
import utils.ErrorModel.{CampaignNotFound, JsonParseError, UnexpectedStatus}

import scala.concurrent.{ExecutionContext, Future}

class CampaignConnectorImpl @Inject()(val wsClient: WSClient, appConfig: AppConfig) extends CampaignConnector {

  val baseUrl: String = appConfig.backendUrl

}

trait CampaignConnector extends Logging {

  val wsClient: WSClient
  val baseUrl: String

  def retrieveAllCampaigns(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Campaign]]] = {
    wsClient.url(s"$baseUrl/campaigns/retrieve").get().map { response =>
      response.status match {
        case OK => response.json.validate[List[Campaign]].fold(
          invalid => {
            logger.error(s"[retrieveAllCampaigns] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NO_CONTENT => Right(List.empty[Campaign])
        case _ => logger.error(s"[retrieveAllCampaigns] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def retrieveSingleCampaign(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    wsClient.url(s"$baseUrl/campaigns/retrieve/$campaignId").get().map { response =>
      response.status match {
        case OK => response.json.validate[Campaign].fold(
          invalid => {
            logger.error(s"[retrieveSingleCampaign] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[retrieveAllCampaigns] Campaign was not found in the backend. Campaign Id: $campaignId")
          Left(CampaignNotFound)
        case _ => logger.error(s"[retrieveAllCampaigns] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def createCampaign(campaign: Campaign)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    wsClient.url(s"$baseUrl/campaigns/create").post(Json.toJson(campaign)).map { response =>
      response.status match {
        case CREATED => response.json.validate[Campaign].fold(
          invalid => {
            logger.error(s"[createCampaign] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case _ => logger.error(s"[createCampaign] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def updateCampaign(campaign: Campaign)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    wsClient.url(s"$baseUrl/campaigns/update").put(Json.toJson(campaign)).map { response =>
      response.status match {
        case OK => response.json.validate[Campaign].fold(
          invalid => {
            logger.error(s"[updateCampaign] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[updateCampaign] Campaign was not found in the backend. Campaign Id: ${campaign.id}")
          Left(CampaignNotFound)
        case _ => logger.error(s"[updateCampaign] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def removeCampaign(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    wsClient.url(s"$baseUrl/campaigns/remove/$campaignId").delete().map { response =>
      response.status match {
        case OK => response.json.validate[Campaign].fold(
          invalid => {
            logger.error(s"[removeCampaign] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[removeCampaign] Campaign was not found in the backend. Campaign Id: $campaignId")
          Left(CampaignNotFound)
        case _ => logger.error(s"[removeCampaign] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

}
