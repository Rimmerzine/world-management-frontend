package controllers.planes

import controllers.FrontendController
import controllers.utils.SessionKeys
import forms.PlaneForm
import javax.inject.Inject
import models.ErrorModel.{CampaignNotFound, ElementNotFound}
import models.{Plane, WorldElement}
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import views.planes.EditPlane

import scala.concurrent.{ExecutionContext, Future}

class EditPlaneControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                        val campaignService: CampaignService,
                                        val editPlane: EditPlane) extends EditPlaneController

trait EditPlaneController extends FrontendController {

  val campaignService: CampaignService
  val editPlane: EditPlane

  val form: Form[(String, Option[String], String)] = PlaneForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, planeId).map {
        case Right(element) =>
          val plane: Plane = element.asInstanceOf[Plane]
          Ok(editPlane(planeId, form.fill(plane.name, plane.description, plane.alignment)))
        case Left(CampaignNotFound | ElementNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

  def submit(planeId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, planeId).flatMap {
        case Right(element) =>
          val plane: Plane = element.asInstanceOf[Plane]
          form.bindFromRequest.fold(
            hasErrors => Future.successful(BadRequest(editPlane(planeId, hasErrors))),
            success => (validSubmit(plane.id, plane.content) _).tupled(success)
          )
        case Left(CampaignNotFound | ElementNotFound) => Future.successful(NotFound)
        case Left(_) => Future.successful(InternalServerError)
      }
    }
  }

  private def validSubmit(planeId: String, content: List[WorldElement])(name: String, description: Option[String], alignment: String)
                         (implicit request: Request[AnyContent]): Future[Result] = {
    val updatedPlane: Plane = Plane("plane", planeId, name, description, content, alignment)
    withNavCollection { (campaignId, journey) =>
      campaignService.replaceElement(campaignId, updatedPlane).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> (journey :+ updatedPlane.id).mkString(","))
        case Left(CampaignNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

}
