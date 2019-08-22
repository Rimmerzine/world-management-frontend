package controllers.planes

import controllers.FrontendController
import forms.PlaneForm
import javax.inject.Inject
import models.Plane
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import utils.ErrorModel.CampaignNotFound
import views.errors.{InternalServerError, NotFound}
import views.planes.CreatePlane

import scala.concurrent.{ExecutionContext, Future}

class CreatePlaneControllerImpl @Inject()(
                                           val controllerComponents: ControllerComponents,
                                           val campaignService: CampaignService,
                                           val createPlane: CreatePlane,
                                           val notFound: NotFound,
                                           val internalServerError: InternalServerError
                                         ) extends CreatePlaneController

trait CreatePlaneController extends FrontendController {

  val campaignService: CampaignService
  val createPlane: CreatePlane
  val notFound: NotFound
  val internalServerError: InternalServerError
  val planeForm: Form[(String, Option[String], String)] = PlaneForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(createPlane(planeForm))
  }

  def submit(): Action[AnyContent] = Action.async { implicit request =>
    planeForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createPlane(hasErrors))),
      success => (validSubmit _).tupled(success)
    )
  }

  private def validSubmit(name: String, description: Option[String], alignment: String)
                         (implicit request: Request[AnyContent]): Future[Result] = {
    val newPlane: Plane = Plane.create(name, description, alignment)

    withNavCollection { (campaignId, journey) =>
      campaignService.addElement(campaignId, journey.reverse.head, newPlane).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(journeyKey -> (journey :+ newPlane.id).mkString(","))
        case Left(CampaignNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

}
