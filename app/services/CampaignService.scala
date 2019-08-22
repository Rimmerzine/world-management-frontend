package services

import connectors.CampaignConnector
import javax.inject.Inject
import models.{Campaign, WorldElement}
import utils.ErrorModel
import utils.ErrorModel.ElementNotFound

import scala.concurrent.{ExecutionContext, Future}

class CampaignServiceImpl @Inject()(val campaignConnector: CampaignConnector) extends CampaignService

trait CampaignService {

  val campaignConnector: CampaignConnector

  def retrieveAllCampaigns(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Campaign]]] = {
    campaignConnector.retrieveAllCampaigns
  }

  def retrieveCampaign(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Campaign]] = {
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

  def retrieveElement(campaignId: String, elementId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, WorldElement]] = {
    retrieveCampaign(campaignId) map {
      case Right(campaign) => campaign.find(elementId).fold[Either[ErrorModel, WorldElement]](Left(ElementNotFound))(Right(_))
      case error => error
    }
  }

  def addElement(campaignId: String, currentElement: String, elementToAdd: WorldElement)
                (implicit ec: ExecutionContext): Future[Either[ErrorModel, WorldElement]] = {
    retrieveCampaign(campaignId).flatMap {
      case Right(campaign) => updateCampaign(campaign.addElementTo(currentElement, elementToAdd).asInstanceOf[Campaign])
      case left => Future.successful(left)
    }
  }

  def removeElement(campaignId: String, elementId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, WorldElement]] = {
    retrieveCampaign(campaignId).flatMap {
      case Right(campaign) => updateCampaign(campaign.removeElementId(elementId).asInstanceOf[Campaign])
      case left => Future.successful(left)
    }
  }

  def replaceElement(campaignId: String, element: WorldElement)(implicit ec: ExecutionContext): Future[Either[ErrorModel, WorldElement]] = {
    retrieveCampaign(campaignId).flatMap {
      case Right(campaign) => updateCampaign(campaign.replace(element.id, element).asInstanceOf[Campaign])
      case left => Future.successful(left)
    }
  }

}
