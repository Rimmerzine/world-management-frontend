package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}

import scala.concurrent.Future

class IndexControllerImpl @Inject()(val controllerComponents: MessagesControllerComponents) extends IndexController

trait IndexController extends FrontendController {

  def index: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(controllers.campaigns.routes.SelectCampaignController.show()))
  }

}
