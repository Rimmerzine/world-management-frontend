package controllers.planes

import controllers.FrontendController
import javax.inject.Inject
import models.ErrorModel.{CampaignNotFound, ElementNotFound}
import models.Plane
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import views.planes.DeletePlane

import scala.concurrent.ExecutionContext

class DeletePlaneControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                          val campaignService: CampaignService,
                                          val deletePlane: DeletePlane) extends DeletePlaneController

trait DeletePlaneController extends FrontendController {

  val campaignService: CampaignService
  val deletePlane: DeletePlane

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, planeId).map {
        case Right(element) => Ok(deletePlane(element.asInstanceOf[Plane]))
        case Left(CampaignNotFound | ElementNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.removeElement(campaignId, planeId).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show())
        case Left(CampaignNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

}
