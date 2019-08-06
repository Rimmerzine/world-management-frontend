package controllers.campaigns

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc._
import services.CampaignService
import utils.ErrorModel.CampaignNotFound
import views.campaigns.DeleteCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.ExecutionContext

class DeleteCampaignControllerImpl @Inject()(
                                              val controllerComponents: ControllerComponents,
                                              val campaignService: CampaignService,
                                              val deleteCampaign: DeleteCampaign,
                                              val internalServerError: InternalServerError,
                                              val notFound: NotFound
                                            ) extends DeleteCampaignController

trait DeleteCampaignController extends FrontendController {

  val campaignService: CampaignService
  val deleteCampaign: DeleteCampaign
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveSingleCampaign(campaignId) map {
      case Right(campaign) => Ok(deleteCampaign(campaign)).as("text/html")
      case Left(CampaignNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.removeCampaign(campaignId).map {
      case Right(_) => Redirect(controllers.campaigns.routes.SelectCampaignController.show())
      case Left(CampaignNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
