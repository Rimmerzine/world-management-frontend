package controllers.lands

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.LandService
import utils.ErrorModel.LandNotFound
import views.errors.{InternalServerError, NotFound}
import views.lands.DeleteLand

import scala.concurrent.ExecutionContext

class DeleteLandControllerImpl @Inject()(
                                          val controllerComponents: ControllerComponents,
                                          val landService: LandService,
                                          val deleteLand: DeleteLand,
                                          val internalServerError: InternalServerError,
                                          val notFound: NotFound
                                        ) extends DeleteLandController

trait DeleteLandController extends FrontendController {

  val landService: LandService
  val deleteLand: DeleteLand
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(landId: String): Action[AnyContent] = Action.async { implicit request =>
    landService.retrieveSingleLand(landId) map {
      case Right(land) => Ok(deleteLand(land)).as("text/html")
      case Left(LandNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(landId: String): Action[AnyContent] = Action.async { implicit request =>
    landService.removeLand(landId) map {
      case Right(land) => Redirect(controllers.lands.routes.SelectLandController.show(land.planeId))
      case Left(LandNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
