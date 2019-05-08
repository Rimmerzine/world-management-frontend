package controllers

import javax.inject.Inject

import play.api.mvc.{Action, AnyContent, ControllerComponents}

import scala.concurrent.Future

class HomeControllerImpl @Inject()(val controllerComponents: ControllerComponents) extends HomeController

trait HomeController extends FrontendController {

  def show: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.home()))
  }

  def start: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(controllers.routes.HomeController.show()))
  }

}