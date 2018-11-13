package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

class IndexControllerImpl @Inject()(val controllerComponents: ControllerComponents) extends IndexController

trait IndexController extends BaseController {

  def index: Action[AnyContent] = Action { implicit request =>
    Redirect(controllers.routes.HomeController.show())
  }

}