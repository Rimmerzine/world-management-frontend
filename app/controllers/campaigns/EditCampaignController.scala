package controllers.campaigns

import controllers.FrontendController
import forms.CampaignForm
import javax.inject.Inject
import models.Campaign
import play.api.data.Form
import play.api.i18n.MessagesProvider
import play.api.mvc._
import services.CampaignService
import utils.ErrorModel.CampaignNotFound
import views.campaigns.EditCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.{ExecutionContext, Future}

class EditCampaignControllerImpl @Inject()(
                                            val controllerComponents: ControllerComponents,
                                            val campaignService: CampaignService,
                                            val editCampaign: EditCampaign,
                                            val internalServerError: InternalServerError,
                                            val notFound: NotFound
                                          ) extends EditCampaignController

trait EditCampaignController extends FrontendController {

  val campaignService: CampaignService
  val editCampaign: EditCampaign
  val internalServerError: InternalServerError
  val notFound: NotFound
  val campaignForm: Form[(String, Option[String])] = CampaignForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveSingleCampaign(campaignId) map {
      case Right(campaign) => Ok(editCampaign(campaignForm.fill(campaign.name, campaign.description), campaignId)).as("text/html")
      case Left(CampaignNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(editCampaign(hasErrors, campaignId)).as("text/html")),
      success => (validSubmit(campaignId) _).tupled(success)
    )
  }

  private def validSubmit(campaignId: String)(name: String, description: Option[String])
                         (implicit messagesProvider: MessagesProvider, request: Request[_]): Future[Result] = {
    val updatedCampaign: Campaign = Campaign(campaignId, name, description)
    campaignService.updateCampaign(updatedCampaign).map {
      case Right(_) => Redirect(controllers.planes.routes.SelectPlaneController.show(campaignId))
      case Left(CampaignNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
