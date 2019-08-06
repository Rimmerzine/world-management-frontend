package controllers.planes

import controllers.FrontendController
import forms.PlaneForm
import javax.inject.Inject
import models.Plane
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import services.PlaneService
import utils.ErrorModel.PlaneNotFound
import views.errors.{InternalServerError, NotFound}
import views.planes.EditPlane

import scala.concurrent.{ExecutionContext, Future}

class EditPlaneControllerImpl @Inject()(
                                         val controllerComponents: ControllerComponents,
                                         val planeService: PlaneService,
                                         val editPlane: EditPlane,
                                         val internalServerError: InternalServerError,
                                         val notFound: NotFound
                                       ) extends EditPlaneController

trait EditPlaneController extends FrontendController {

  val planeService: PlaneService
  val editPlane: EditPlane
  val internalServerError: InternalServerError
  val notFound: NotFound
  val form: Form[(String, Option[String], String)] = PlaneForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    planeService.retrieveSinglePlane(planeId) map {
      case Right(plane) => Ok(editPlane(planeId, form.fill(plane.name, plane.description, plane.alignment))).as("text/html")
      case Left(PlaneNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    planeService.retrieveSinglePlane(planeId) flatMap {
      case Right(plane) => form.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest(editPlane(planeId, hasErrors)).as("text/html")),
        success => (validSubmit(plane.campaignId, planeId) _).tupled(success)
      )
      case Left(PlaneNotFound) => Future.successful(NotFound(notFound()).as("text/html"))
      case Left(_) => Future.successful(InternalServerError(internalServerError()).as("text/html"))
    }
  }

  private def validSubmit(campaignId: String, planeId: String)(name: String, description: Option[String], alignment: String): Future[Result] = {
    val updatedPlane: Plane = Plane(campaignId, planeId, name, description, alignment)
    planeService.updatePlane(updatedPlane) map {
      case Right(_) => Redirect(controllers.lands.routes.SelectLandController.show(planeId))
      case Left(PlaneNotFound) => NotFound(notFound()).as("text/html")
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
    }
  }

}
