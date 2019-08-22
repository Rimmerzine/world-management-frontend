package controllers.lands

import controllers.FrontendController
import javax.inject.Inject
import models.Land
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, ElementNotFound}
import views.errors.{InternalServerError, NotFound}
import views.lands.DeleteLand

import scala.concurrent.ExecutionContext

class DeleteLandControllerImpl @Inject()(
                                          val controllerComponents: ControllerComponents,
                                          val campaignService: CampaignService,
                                          val deleteLand: DeleteLand,
                                          val internalServerError: InternalServerError,
                                          val notFound: NotFound
                                        ) extends DeleteLandController

trait DeleteLandController extends FrontendController {

  val campaignService: CampaignService
  val deleteLand: DeleteLand
  val internalServerError: InternalServerError
  val notFound: NotFound

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(landId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.retrieveElement(campaignId, landId).map {
        case Right(element) => Ok(deleteLand(element.asInstanceOf[Land]))
        case Left(CampaignNotFound | ElementNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

  def submit(landId: String): Action[AnyContent] = Action.async { implicit request =>
    withNavCollection { (campaignId, _) =>
      campaignService.removeElement(campaignId, landId).map {
        case Right(_) => Redirect(controllers.routes.SelectController.show())
        case Left(CampaignNotFound) => NotFound(notFound())
        case Left(_) => InternalServerError(internalServerError())
      }
    }
  }

}
