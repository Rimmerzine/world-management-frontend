package controllers

import controllers.utils.SessionKeys
import javax.inject.Inject
import models.ErrorModel.{CampaignNotFound, ElementNotFound}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import views.SelectElement

import scala.concurrent.{ExecutionContext, Future}

class SelectControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                     val campaignService: CampaignService,
                                     val selectElement: SelectElement) extends SelectController

trait SelectController extends FrontendController {

  val campaignService: CampaignService
  val selectElement: SelectElement

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, journey) =>
      campaignService.retrieveElement(campaignId, journey.reverse.head).map {
        case Right(element) => Ok(selectElement(campaignId, element))
        case Left(CampaignNotFound | ElementNotFound) => NotFound
        case Left(_) => InternalServerError
      }
    }
  }

  def view(id: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (_, journey) =>
      Future.successful(Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> (journey :+ id).mkString(",")))
    }
  }

  def back(): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, journey) =>
      Future.successful(
        Redirect(controllers.routes.SelectController.show()).addingToSession(SessionKeys.journey -> journey.reverse.tail.reverse.mkString(","))
      )
    }
  }

}
