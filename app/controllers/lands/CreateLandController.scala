package controllers.lands

import controllers.FrontendController
import forms.LandForm
import javax.inject.Inject
import models.Land
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import services.LandService
import views.errors.InternalServerError
import views.lands.CreateLand

import scala.concurrent.{ExecutionContext, Future}

class CreateLandControllerImpl @Inject()(
                                          val controllerComponents: ControllerComponents,
                                          val landService: LandService,
                                          val createLand: CreateLand,
                                          val internalServerError: InternalServerError
                                        ) extends CreateLandController

trait CreateLandController extends FrontendController {

  val landService: LandService
  val createLand: CreateLand
  val internalServerError: InternalServerError
  val landForm: Form[(String, Option[String])] = LandForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action { implicit request =>
    Ok(createLand(planeId, landForm)).as("text/html")
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    landForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createLand(planeId, hasErrors)).as("text/html")),
      success => (validSubmit(planeId) _).tupled(success)
    )
  }

  private def validSubmit(planeId: String)(name: String, description: Option[String]): Future[Result] = {
    val newLand: Land = Land.create(planeId, name, description)
    landService.createLand(newLand) map {
      case Left(_) => InternalServerError(internalServerError()).as("text/html")
      case Right(_) => Redirect(controllers.lands.routes.SelectLandController.show(newLand.planeId))
    }
  }

}
