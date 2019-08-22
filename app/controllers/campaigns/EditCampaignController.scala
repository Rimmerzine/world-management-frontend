package controllers.campaigns

import controllers.FrontendController
import forms.CampaignForm
import javax.inject.Inject
import models.{Campaign, WorldElement}
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
    campaignService.retrieveCampaign(campaignId) map {
      case Right(campaign) => Ok(editCampaign(campaignForm.fill(campaign.name, campaign.description), campaignId))
      case Left(CampaignNotFound) => NotFound(notFound())
      case Left(_) => InternalServerError(internalServerError())
    }
  }

  def submit(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveCampaign(campaignId) flatMap {
      case Right(campaign) => campaignForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest(editCampaign(hasErrors, campaignId))),
        success => (validSubmit(campaignId, campaign.content) _).tupled(success)
      )
      case Left(CampaignNotFound) => Future.successful(NotFound(notFound()))
      case Left(_) => Future.successful(InternalServerError(internalServerError()))
    }
  }

  private def validSubmit(campaignId: String, content: List[WorldElement])(name: String, description: Option[String])
                         (implicit messagesProvider: MessagesProvider, request: Request[_]): Future[Result] = {

    val updatedCampaign: Campaign = Campaign("campaign", campaignId, name, description, content)

    campaignService.updateCampaign(updatedCampaign).map {
      case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(journeyKey -> updatedCampaign.id)
      case Left(CampaignNotFound) => NotFound(notFound())
      case Left(_) => InternalServerError(internalServerError())
    }

  }

}
