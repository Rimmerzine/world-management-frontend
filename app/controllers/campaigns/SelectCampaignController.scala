package controllers.campaigns

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import views.campaigns.SelectCampaign
import views.errors.InternalServerError

import scala.concurrent.{ExecutionContext, Future}

class SelectCampaignControllerImpl @Inject()(
                                              val controllerComponents: ControllerComponents,
                                              val campaignService: CampaignService,
                                              val selectCampaign: SelectCampaign,
                                              val internalServerError: InternalServerError
                                            ) extends SelectCampaignController

trait SelectCampaignController extends FrontendController {

  val campaignService: CampaignService
  val selectCampaign: SelectCampaign
  val internalServerError: InternalServerError

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show: Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveAllCampaigns.map {
      case Right(campaigns) => Ok(selectCampaign(campaigns)).removingFromSession(journeyKey)
      case Left(_) => InternalServerError(internalServerError())
    }
  }

  def view(id: String): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(controllers.routes.SelectController.show()).addingToSession(journeyKey -> id))
  }

}
