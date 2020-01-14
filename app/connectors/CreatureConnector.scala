package connectors

import config.AppConfig
import javax.inject.Inject
import models.ErrorModel.{CreatureNotFound, JsonParseError, UnexpectedStatus}
import models.{Creature, ErrorModel}
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class CreatureConnectorImpl @Inject()(val wsClient: WSClient, appConfig: AppConfig) extends CreatureConnector {

  val baseUrl: String = appConfig.backendUrl

}

trait CreatureConnector extends Logging {

  val wsClient: WSClient
  val baseUrl: String

  def retrieveAllCreatures(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Creature]]] = {
    wsClient.url(s"$baseUrl/creatures/retrieve").get().map { response =>
      response.status match {
        case OK => response.json.validate[List[Creature]].fold(
          invalid => {
            logger.error(s"[retrieveAllCreatures] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NO_CONTENT => Right(List.empty[Creature])
        case _ => logger.error(s"[retrieveAllCreatures] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def retrieveSingleCreature(creatureId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    wsClient.url(s"$baseUrl/creatures/retrieve/$creatureId").get().map { response =>
      response.status match {
        case OK => response.json.validate[Creature].fold(
          invalid => {
            logger.error(s"[retrieveSingleCreature] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[retrieveSingleCreature] Creature was not found in the backend. Creature Id: $creatureId")
          Left(CreatureNotFound)
        case _ => logger.error(s"[retrieveSingleCreature] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def createCreature(creature: Creature)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    wsClient.url(s"$baseUrl/creatures/create").post(Json.toJson(creature)).map { response =>
      response.status match {
        case CREATED => response.json.validate[Creature].fold(
          invalid => {
            logger.error(s"[createCreature] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case _ => logger.error(s"[createCreature] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def updateCreature(creature: Creature)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    wsClient.url(s"$baseUrl/creatures/update").put(Json.toJson(creature)).map { response =>
      response.status match {
        case OK => response.json.validate[Creature].fold(
          invalid => {
            logger.error(s"[updateCreature] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[updateCreature] Creature was not found in the backend. Creature Id: ${creature.id}")
          Left(CreatureNotFound)
        case _ => logger.error(s"[updateCreature] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def removeCreature(creatureId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Creature]] = {
    wsClient.url(s"$baseUrl/creatures/remove/$creatureId").delete().map { response =>
      response.status match {
        case OK => response.json.validate[Creature].fold(
          invalid => {
            logger.error(s"[removeCreature] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[removeCreature] Creature was not found in the backend. Creature Id: $creatureId")
          Left(CreatureNotFound)
        case _ => logger.error(s"[removeCreature] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

}
