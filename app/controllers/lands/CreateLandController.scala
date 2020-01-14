package controllers.lands

import controllers.FrontendController
import controllers.utils.SessionKeys
import forms.LandForm
import javax.inject.Inject
import models.ErrorModel.CampaignNotFound
import models.Land
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import views.lands.CreateLand

import scala.concurrent.{ExecutionContext, Future}

class CreateLandControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                         val campaignService: CampaignService,
                                         val createLand: CreateLand) extends CreateLandController

trait CreateLandController extends FrontendController {

  val campaignService: CampaignService
  val createLand: CreateLand

  val landForm: Form[(String, Option[String])] = LandForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show: Action[AnyContent] = Action { implicit request =>
    Ok(createLand(landForm))
  }

  def submit: Action[AnyContent] = Action.async { implicit request =>
    landForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createLand(hasErrors))),
      success => (validSubmit _).tupled(success)
    )
  }

  private def validSubmit(name: String, description: Option[String])(implicit request: Request[AnyContent]): Future[Result] = {
    val newLand: Land = Land.create(name, description)

    withNavCollection { (campaignId, journey) =>
      campaignService.addElement(campaignId, journey.reverse.head, newLand) map {
        case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> (journey :+ newLand.id).mkString(","))
        case Left(CampaignNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

}
