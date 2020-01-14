package controllers.campaigns

import controllers.FrontendController
import controllers.utils.SessionKeys
import forms.CampaignForm
import javax.inject.Inject
import models.Campaign
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import views.campaigns.CreateCampaign

import scala.concurrent.{ExecutionContext, Future}

class CreateCampaignControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                             val campaignService: CampaignService,
                                             val createCampaign: CreateCampaign) extends CreateCampaignController

trait CreateCampaignController extends FrontendController {

  val campaignService: CampaignService
  val createCampaign: CreateCampaign
  val campaignForm: Form[(String, Option[String])] = CampaignForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show: Action[AnyContent] = Action { implicit request =>
    Ok(createCampaign(campaignForm))
  }

  def submit: Action[AnyContent] = Action.async { implicit request =>
    campaignForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createCampaign(hasErrors))),
      success => (validSubmit _).tupled(success)
    )
  }

  private def validSubmit(name: String, description: Option[String])(implicit request: Request[_]): Future[Result] = {
    val newCampaign: Campaign = Campaign.create(name, description)
    campaignService.createCampaign(newCampaign).map {
      case Left(_) => InternalServerError
      case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> newCampaign.id)
    }
  }

}
