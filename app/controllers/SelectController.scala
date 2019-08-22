package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, ElementNotFound}
import views.SelectElement
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.{ExecutionContext, Future}

class SelectControllerImpl @Inject()(
                                      val controllerComponents: ControllerComponents,
                                      val campaignService: CampaignService,
                                      val selectElement: SelectElement,
                                      val notFound: NotFound,
                                      val internalServerError: InternalServerError
                                    ) extends SelectController

trait SelectController extends FrontendController {

  val campaignService: CampaignService
  val selectElement: SelectElement
  val notFound: NotFound
  val internalServerError: InternalServerError

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, journey) =>
      campaignService.retrieveElement(campaignId, journey.reverse.head).map {
        case Right(element) => Ok(selectElement(campaignId, element))
        case Left(CampaignNotFound | ElementNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

  def view(id: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (_, journey) =>
      Future.successful(Redirect(controllers.routes.SelectController.show()).addingToSession(journeyKey -> (journey :+ id).mkString(",")))
    }
  }

  def back(): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, journey) =>
      Future.successful(
        Redirect(controllers.routes.SelectController.show()).addingToSession(journeyKey -> journey.reverse.tail.reverse.mkString(","))
      )
    }
  }

}
