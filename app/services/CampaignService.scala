package services

import connectors.CampaignConnector
import javax.inject.Inject
import models.Campaign
import utils.ErrorModel

import scala.concurrent.{ExecutionContext, Future}

class CampaignServiceImpl @Inject()(val campaignConnector: CampaignConnector) extends CampaignService

trait CampaignService {

  val campaignConnector: CampaignConnector

  def retrieveAllCampaigns(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Campaign]]] = {
    campaignConnector.retrieveAllCampaigns
  }

  def retrieveSingleCampaign(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    campaignConnector.retrieveSingleCampaign(campaignId)
  }

  def createCampaign(campaign: Campaign)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    campaignConnector.createCampaign(campaign)
  }

  def updateCampaign(campaign: Campaign)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    campaignConnector.updateCampaign(campaign)
  }

  def removeCampaign(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
    campaignConnector.removeCampaign(campaignId)
  }

}
