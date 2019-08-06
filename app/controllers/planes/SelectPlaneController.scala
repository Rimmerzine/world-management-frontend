package controllers.planes

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.{CampaignService, PlaneService}
import utils.ErrorModel.CampaignNotFound
import views.errors.{InternalServerError, NotFound}
import views.planes.SelectPlane

import scala.concurrent.{ExecutionContext, Future}

class SelectPlaneControllerImpl @Inject()(
                                           val controllerComponents: ControllerComponents,
                                           val campaignService: CampaignService,
                                           val planeService: PlaneService,
                                           val selectPlane: SelectPlane,
                                           val internalServerError: InternalServerError,
                                           val notFound: NotFound
                                         ) extends SelectPlaneController

trait SelectPlaneController extends FrontendController {

  val campaignService: CampaignService
  val planeService: PlaneService
  val selectPlane: SelectPlane
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(campaignId: String): Action[AnyContent] = Action.async { implicit request =>
    campaignService.retrieveSingleCampaign(campaignId) flatMap {
      case Right(campaign) => planeService.retrieveAllPlanes(campaignId) map {
        case Right(planes) => Ok(selectPlane(campaign, planes)).as("text/html")
        case Left(_) => InternalServerError(internalServerError()).as("text/html")
      }
      case Left(CampaignNotFound) => Future.successful(NotFound(notFound()).as("text/html"))
      case Left(_) => Future.successful(InternalServerError(internalServerError()).as("text/html"))
    }
  }

}
