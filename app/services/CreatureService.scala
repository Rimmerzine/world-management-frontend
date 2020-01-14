package services

import connectors.CreatureConnector
import javax.inject.Inject
import models.{Creature, ErrorModel}

import scala.concurrent.{ExecutionContext, Future}

class CreatureServiceImpl @Inject()(val creatureConnector: CreatureConnector) extends CreatureService

trait CreatureService {

  val creatureConnector: CreatureConnector

  def retrieveAllCreatures(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Creature]]] = {
    creatureConnector.retrieveAllCreatures
  }

  def retrieveCreature(creatureId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    creatureConnector.retrieveSingleCreature(creatureId)
  }

  def createCreature(creature: Creature)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    creatureConnector.createCreature(creature)
  }

  def updateCreature(creature: Creature)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    creatureConnector.updateCreature(creature)
  }

  def removeCreature(creatureId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    creatureConnector.removeCreature(creatureId)
  }

}
