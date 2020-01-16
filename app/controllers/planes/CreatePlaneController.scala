package controllers.planes

import controllers.FrontendController
import controllers.utils.SessionKeys
import forms.PlaneForm
import javax.inject.Inject
import models.ErrorModel.CampaignNotFound
import models.Plane
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import views.planes.CreatePlane

import scala.concurrent.{ExecutionContext, Future}

class CreatePlaneControllerImpl @Inject()(val controllerComponents: MessagesControllerComponents,
                                          val campaignService: CampaignService,
                                          val createPlane: CreatePlane) extends CreatePlaneController

trait CreatePlaneController extends FrontendController {

  val campaignService: CampaignService
  val createPlane: CreatePlane
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
        case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> (journey :+ newPlane.id).mkString(","))
        case Left(CampaignNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

}
