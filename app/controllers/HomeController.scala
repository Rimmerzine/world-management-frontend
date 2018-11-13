package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}

class HomeControllerImpl @Inject()(val controllerComponents: ControllerComponents) extends HomeController

trait HomeController extends FrontendController {

  def show: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.home())
  }

  def start: Action[AnyContent] = Action { implicit request =>
    Redirect(controllers.routes.HomeController.show())
  }

}