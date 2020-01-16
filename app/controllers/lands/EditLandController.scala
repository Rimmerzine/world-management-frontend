package controllers.lands

import controllers.FrontendController
import controllers.utils.SessionKeys
import forms.LandForm
import javax.inject.Inject
import models.ErrorModel.{CampaignNotFound, ElementNotFound}
import models.{Land, WorldElement}
import play.api.data.Form
import play.api.mvc._
import services.CampaignService
import views.lands.EditLand

import scala.concurrent.{ExecutionContext, Future}

class EditLandControllerImpl @Inject()(val controllerComponents: MessagesControllerComponents,
                                       val campaignService: CampaignService,
                                       val editLand: EditLand) extends EditLandController

trait EditLandController extends FrontendController {

  val campaignService: CampaignService
  val editLand: EditLand
  val landForm: Form[(String, Option[String])] = LandForm.form

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(landId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, landId).map {
        case Right(land) => Ok(editLand(landId, landForm.fill(land.name, land.description)))
        case Left(CampaignNotFound | ElementNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

  def submit(landId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, landId).flatMap {
        case Right(element) =>
          val land: Land = element.asInstanceOf[Land]
          landForm.bindFromRequest.fold(
            hasErrors => Future.successful(BadRequest(editLand(landId, hasErrors))),
            success => (validSubmit(land.id, land.content) _).tupled(success)
          )
        case Left(CampaignNotFound | ElementNotFound) => Future.successful(NotFound)
        case Left(_) => Future.successful(InternalServerError)
      }
    }
  }

  private def validSubmit(landId: String, content: List[WorldElement])(name: String, description: Option[String])
                         (implicit request: Request[AnyContent]): Future[Result] = {
    val updatedLand = Land("land", landId, name, description, content)
    withNavCollection { (campaignId, journey) =>
      campaignService.replaceElement(campaignId, updatedLand).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> (journey :+ updatedLand.id).mkString(","))
        case Left(CampaignNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

}
