package controllers.planes

import controllers.FrontendController
import javax.inject.Inject
import models.Plane
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, ElementNotFound}
import views.errors.{InternalServerError, NotFound}
import views.planes.DeletePlane

import scala.concurrent.ExecutionContext

class DeletePlaneControllerImpl @Inject()(
                                           val controllerComponents: ControllerComponents,
                                           val campaignService: CampaignService,
                                           val deletePlane: DeletePlane,
                                           val internalServerError: InternalServerError,
                                           val notFound: NotFound
                                         ) extends DeletePlaneController

trait DeletePlaneController extends FrontendController {

  val campaignService: CampaignService
  val deletePlane: DeletePlane
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, planeId).map {
        case Right(element) => Ok(deletePlane(element.asInstanceOf[Plane]))
        case Left(CampaignNotFound | ElementNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.removeElement(campaignId, planeId).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show())
        case Left(CampaignNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

}
