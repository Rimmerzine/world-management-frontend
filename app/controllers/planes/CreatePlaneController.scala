package controllers.planes

import config.AppConfig
import controllers.FrontendController
import forms.PlaneForm
import javax.inject.Inject
import models.Plane
import play.api.data.Form
import play.api.i18n.MessagesProvider
import play.api.mvc._
import services.PlaneService
import views.errors.InternalServerError
import views.planes.CreatePlane

import scala.concurrent.{ExecutionContext, Future}

class CreatePlaneControllerImpl @Inject()(
                                              val controllerComponents: ControllerComponents,
                                              val planeService: PlaneService,
                                              val appConfig: AppConfig,
                                              val createPlane: CreatePlane,
                                              val internalServerError: InternalServerError
                                            ) extends CreatePlaneController

trait CreatePlaneController extends FrontendController {

  val planeService: PlaneService
  val createPlane: CreatePlane
  val internalServerError: InternalServerError

  implicit val appConfig: AppConfig
  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  val planeForm: Form[(String, Option[String], String)] = PlaneForm.form

  def show(campaignId: String): Action[AnyContent] = Action { implicit request =>
    Ok(createPlane(campaignId, planeForm)).as("text/html")
  }

  def submit(campaignId: String) :Action[AnyContent] = Action.async { implicit request =>
    planeForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createPlane(campaignId, hasErrors)).as("text/html")),
      success => (validSubmit(campaignId) _).tupled(success)
    )
  }

  private def validSubmit(campaignId: String)(name: String, description: Option[String], alignment: String)
                         (implicit messagesProvider: MessagesProvider, request: Request[_]): Future[Result] = {
    val newPlane: Plane = Plane.create(campaignId, name, description, alignment)
    planeService.createPlane(newPlane).map {
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
      case Right(_) => Redirect(controllers.planes.routes.SelectPlaneController.show(newPlane.campaignId))
    }
  }

}
