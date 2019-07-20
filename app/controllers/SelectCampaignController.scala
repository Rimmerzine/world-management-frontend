package controllers

import config.AppConfig
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import views.campaigns.SelectCampaign
import views.errors.InternalServerError

import scala.concurrent.ExecutionContext

class SelectCampaignControllerImpl @Inject()(
                                              val controllerComponents: ControllerComponents,
                                              val campaignService: CampaignService,
                                              val appConfig: AppConfig,
                                              val selectCampaign: SelectCampaign,
                                              val internalServerError: InternalServerError
                                            ) extends SelectCampaignController

trait SelectCampaignController extends FrontendController {

  val campaignService: CampaignService
  val selectCampaign: SelectCampaign
  val internalServerError: InternalServerError

  implicit val appConfig: AppConfig
  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show: Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveAllCampaigns.map {
      case Right(campaigns) => Ok(selectCampaign(campaigns)).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
