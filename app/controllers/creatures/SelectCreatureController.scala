package controllers.creatures

import controllers.FrontendController
import forms.SelectCreatureForm
import javax.inject.Inject
import models.Creature
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CreatureService
import views.creatures.SelectCreature

import scala.concurrent.ExecutionContext

class SelectCreatureControllerImpl @Inject()(val controllerComponents: MessagesControllerComponents,
                                             val creatureService: CreatureService,
                                             val selectCreature: SelectCreature) extends SelectCreatureController

trait SelectCreatureController extends FrontendController {

  val creatureService: CreatureService
  val selectCreature: SelectCreature

  implicit lazy val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[(String, String)] = SelectCreatureForm.selectCreatureForm

  private def distinctChallengeRatings(creatures: List[Creature]): List[String] = creatures.map(_.detail.challengeRating.toString).distinct

  private def distinctNameStarts(creatures: List[Creature]): List[String] = creatures.map(_.detail.name.head.toUpper.toString).distinct

  def show: Action[AnyContent] = Action.async { implicit request =>
    creatureService.retrieveAllCreatures.map {
      case Right(creatures) => Ok(selectCreature(form, creatures, distinctChallengeRatings(creatures), distinctNameStarts(creatures)))
      case Left(_) => InternalServerError
    }
  }

  def filter: Action[AnyContent] = Action.async { implicit request =>
    creatureService.retrieveAllCreatures map {
      case Right(creatures) =>
        val possibleNameStarts = distinctNameStarts(creatures)
        val possibleChallengeRatings = distinctChallengeRatings(creatures)
        form.bindFromRequest.fold(
          hasErrors => BadRequest(selectCreature(hasErrors, creatures, possibleChallengeRatings, possibleNameStarts)), { case (nameStart, challengeRating) =>
            Ok(selectCreature(
              form.fill(nameStart, challengeRating),
              filterCreatures(creatures, Some(nameStart).filterNot(_ == "none"), Some(challengeRating).filterNot(_ == "none")),
              possibleChallengeRatings,
              possibleNameStarts
            ))
          }
        )
      case Left(_) => InternalServerError
    }
  }

  private def filterCreatures(creatures: List[Creature], nameStart: Option[String], challengeRating: Option[String]): List[Creature] = {
    creatures filter { creature =>
      nameStart.fold(true)(_ == creature.detail.name.head.toUpper.toString)
    } filter { creature =>
      challengeRating.fold(true)(_ == creature.detail.challengeRating.toString)
    }
  }

}
