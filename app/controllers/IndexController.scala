package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}

class IndexControllerImpl @Inject()(val controllerComponents: ControllerComponents) extends IndexController

trait IndexController extends FrontendController {

  val index: Action[AnyContent] = Action { implicit request =>
    Redirect(controllers.routes.SelectCampaignController.show())
  }

}
