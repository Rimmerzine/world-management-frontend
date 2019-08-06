package controllers.planes

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.PlaneService
import utils.ErrorModel.PlaneNotFound
import views.errors.{InternalServerError, NotFound}
import views.planes.DeletePlane

import scala.concurrent.ExecutionContext

class DeletePlaneControllerImpl @Inject()(
                                           val controllerComponents: ControllerComponents,
                                           val planeService: PlaneService,
                                           val deletePlane: DeletePlane,
                                           val internalServerError: InternalServerError,
                                           val notFound: NotFound
                                         ) extends DeletePlaneController

trait DeletePlaneController extends FrontendController {

  val planeService: PlaneService
  val deletePlane: DeletePlane
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    planeService.retrieveSinglePlane(planeId) map {
      case Right(plane) => Ok(deletePlane(plane)).as("text/html")
      case Left(PlaneNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    planeService.removePlane(planeId) map {
      case Right(plane) => Redirect(controllers.planes.routes.SelectPlaneController.show(plane.campaignId))
      case Left(PlaneNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
