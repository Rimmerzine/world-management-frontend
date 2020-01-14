package controllers.creatures

import controllers.FrontendController
import forms.CreatureForm
import javax.inject.Inject
import models.{Creature, CreatureDetail}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CreatureService
import views.creatures.CreateCreature

import scala.concurrent.{ExecutionContext, Future}

class CreateCreatureControllerImpl @Inject()(val controllerComponents: ControllerComponents,
                                             val creatureService: CreatureService,
                                             val createCreature: CreateCreature) extends CreateCreatureController

trait CreateCreatureController extends FrontendController {

  val creatureService: CreatureService
  val createCreature: CreateCreature

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[CreatureDetail] = CreatureForm.creatureForm

  def show: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(createCreature(form)))
  }

  def submit: Action[AnyContent] = Action.async { implicit request =>
    form.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest(createCreature(hasErrors))),
      success => {
        val newCreature = Creature.create(success)
        creatureService.createCreature(newCreature) map {
          case Right(_) => Redirect(controllers.creatures.routes.SelectCreatureController.show())
          case Left(_) => InternalServerError
        }
      }
    )
  }

}
