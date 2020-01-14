package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import views.Tools

import scala.concurrent.{ExecutionContext, Future}

class ToolsControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                    val tools: Tools) extends ToolsController

trait ToolsController extends FrontendController {

  val tools: Tools

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  def show(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(tools()))
  }

}
