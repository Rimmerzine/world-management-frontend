package controllers.lands

import controllers.FrontendController
import forms.LandForm
import javax.inject.Inject
import models.Land
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import services.LandService
import utils.ErrorModel.LandNotFound
import views.errors.{InternalServerError, NotFound}
import views.lands.EditLand

import scala.concurrent.{ExecutionContext, Future}

class EditLandControllerImpl @Inject()(
                                        val controllerComponents: ControllerComponents,
                                        val landService: LandService,
                                        val editLand: EditLand,
                                        val internalServerError: InternalServerError,
                                        val notFound: NotFound
                                      ) extends EditLandController

trait EditLandController extends FrontendController {

  val landService: LandService
  val editLand: EditLand
  val internalServerError: InternalServerError
  val notFound: NotFound
  val landForm: Form[(String, Option[String])] = LandForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(landId: String): Action[AnyContent] = Action.async { implicit request =>
    landService.retrieveSingleLand(landId) map {
      case Right(land) => Ok(editLand(landId, landForm.fill(land.name, land.description))).as("text/html")
      case Left(LandNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(landId: String): Action[AnyContent] = Action.async { implicit request =>
    landService.retrieveSingleLand(landId) flatMap {
      case Right(land) => landForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest(editLand(landId, hasErrors)).as("text/html")),
        success => (validSubmit(land.planeId, land.landId) _).tupled(success)
      )
      case Left(LandNotFound) => Future.successful(NotFound(notFound()).as("text/html"))
      case Left(_) => Future.successful(InternalServerError(internalServerError()).as("text/html"))
    }
  }

  private def validSubmit(planeId: String, landId: String)(name: String, description: Option[String]): Future[Result] = {
    val updatedLand = Land(planeId, landId, name, description)
    landService.updateLand(updatedLand) map {
      case Left(LandNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
      case Right(_) => Redirect(controllers.lands.routes.SelectLandController.show(updatedLand.planeId))
    }
  }

}
