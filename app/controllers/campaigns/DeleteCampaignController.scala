package controllers.campaigns

import controllers.FrontendController
import javax.inject.Inject
import models.ErrorModel.CampaignNotFound
import play.api.mvc._
import services.CampaignService
import views.campaigns.DeleteCampaign

import scala.concurrent.ExecutionContext

class DeleteCampaignControllerImpl @Inject()(val controllerComponents: MessagesControllerComponents,
                                             val campaignService: CampaignService,
                                             val deleteCampaign: DeleteCampaign) extends DeleteCampaignController

trait DeleteCampaignController extends FrontendController {

  val campaignService: CampaignService
  val deleteCampaign: DeleteCampaign

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveCampaign(campaignId) map {
      case Right(campaign) => Ok(deleteCampaign(campaign))
      case Left(CampaignNotFound) => NotFound
      case Left(_) => InternalServerError
    }
  }

  def submit(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.removeCampaign(campaignId).map {
      case Right(_) => Redirect(controllers.campaigns.routes.SelectCampaignController.show())
      case Left(CampaignNotFound) => NotFound
      case Left(_) => InternalServerError
    }
  }

}
