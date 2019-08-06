package connectors

import config.AppConfig
import javax.inject.Inject
import models.Land
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import utils.ErrorModel
import utils.ErrorModel.{JsonParseError, LandNotFound, UnexpectedStatus}

import scala.concurrent.{ExecutionContext, Future}

class LandConnectorImpl @Inject()(val wsClient: WSClient, appConfig: AppConfig) extends LandConnector {

  val baseUrl: String = appConfig.backendUrl

}

trait LandConnector extends Logging {

  val wsClient: WSClient
  val baseUrl: String

  def retrieveAllLands(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Land]]] = {
    wsClient.url(s"$baseUrl/plane/$planeId/lands").get().map { response =>
      response.status match {
        case OK => response.json.validate[List[Land]].fold(
          invalid => {
            logger.error(s"[retrieveAllLands] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NO_CONTENT => Right(List.empty[Land])
        case _ => logger.error(s"[retrieveAllLands] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def retrieveSingleLand(landId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    wsClient.url(s"$baseUrl/lands/retrieve/$landId").get().map { response =>
      response.status match {
        case OK => response.json.validate[Land].fold(
          invalid => {
            logger.error(s"[retrieveSingleLand] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[retrieveAllLands] Land was not found in the backend. Land Id: $landId")
          Left(LandNotFound)
        case _ => logger.error(s"[retrieveAllLands] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def createLand(land: Land)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    wsClient.url(s"$baseUrl/lands/create").post(Json.toJson(land)).map { response =>
      response.status match {
        case CREATED => response.json.validate[Land].fold(
          invalid => {
            logger.error(s"[createLand] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case _ => logger.error(s"[createLand] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def updateLand(land: Land)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    wsClient.url(s"$baseUrl/lands/update").put(Json.toJson(land)).map { response =>
      response.status match {
        case OK => response.json.validate[Land].fold(
          invalid => {
            logger.error(s"[updateLand] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[updateLand] Land was not found in the backend. Land Id: ${land.landId}")
          Left(LandNotFound)
        case _ => logger.error(s"[updateLand] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def removeLand(landId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    wsClient.url(s"$baseUrl/lands/remove/$landId").delete().map { response =>
      response.status match {
        case OK => response.json.validate[Land].fold(
          invalid => {
            logger.error(s"[removeLand] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[removeLand] Land was not found in the backend. Land Id: $landId")
          Left(LandNotFound)
        case _ => logger.error(s"[removeLand] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

}
