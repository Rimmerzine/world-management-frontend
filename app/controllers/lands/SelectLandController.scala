package controllers.lands

import controllers.FrontendController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.{LandService, PlaneService}
import utils.ErrorModel.PlaneNotFound
import views.errors.{InternalServerError, NotFound}
import views.lands.SelectLand

import scala.concurrent.{ExecutionContext, Future}

class SelectLandControllerImpl @Inject()(
                                          val controllerComponents: ControllerComponents,
                                          val planeService: PlaneService,
                                          val landService: LandService,
                                          val selectLand: SelectLand,
                                          val notFound: NotFound,
                                          val internalServerError: InternalServerError
                                        ) extends SelectLandController

trait SelectLandController extends FrontendController {

  val planeService: PlaneService
  val landService: LandService
  val selectLand: SelectLand
  val notFound: NotFound
  val internalServerError: InternalServerError

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    planeService.retrieveSinglePlane(planeId) flatMap {
      case Right(plane) => landService.retrieveAllLands(planeId) map {
        case Right(lands) => Ok(selectLand(plane, lands)).as("text/html")
        case Left(_) => InternalServerError(internalServerError()).as("text/html")
      }
      case Left(PlaneNotFound) => Future.successful(NotFound(notFound()).as("text/html"))
      case Left(_) => Future.successful(InternalServerError(internalServerError()).as("text/html"))
    }
  }

}
