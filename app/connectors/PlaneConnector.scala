package connectors

import config.AppConfig
import javax.inject.Inject
import models.Plane
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import utils.ErrorModel
import utils.ErrorModel.{PlaneNotFound, JsonParseError, UnexpectedStatus}

import scala.concurrent.{ExecutionContext, Future}

class PlaneConnectorImpl @Inject()(val wsClient: WSClient, appConfig: AppConfig) extends PlaneConnector {

  val baseUrl: String = appConfig.backendUrl

}

trait PlaneConnector extends Logging {

  val wsClient: WSClient
  val baseUrl: String

  def retrieveAllPlanes(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Plane]]] = {
    wsClient.url(s"$baseUrl/campaign/$campaignId/planes").get().map { response =>
      response.status match {
        case OK => response.json.validate[List[Plane]].fold(
          invalid => {
            logger.error(s"[retrieveAllPlanes] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NO_CONTENT => Right(List.empty[Plane])
        case _ => logger.error(s"[retrieveAllPlanes] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def retrieveSinglePlane(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    wsClient.url(s"$baseUrl/planes/retrieve/$planeId").get().map { response =>
      response.status match {
        case OK => response.json.validate[Plane].fold(
          invalid => {
            logger.error(s"[retrieveSinglePlane] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[retrieveAllPlanes] Plane was not found in the backend. Plane Id: $planeId")
          Left(PlaneNotFound)
        case _ => logger.error(s"[retrieveAllPlanes] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def createPlane(plane: Plane)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    wsClient.url(s"$baseUrl/planes/create").post(Json.toJson(plane)).map { response =>
      response.status match {
        case CREATED => response.json.validate[Plane].fold(
          invalid => {
            logger.error(s"[createPlane] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case _ => logger.error(s"[createPlane] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def updatePlane(plane: Plane)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    wsClient.url(s"$baseUrl/planes/update").put(Json.toJson(plane)).map { response =>
      response.status match {
        case OK => response.json.validate[Plane].fold(
          invalid => {
            logger.error(s"[updatePlane] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[updatePlane] Plane was not found in the backend. Plane Id: ${plane.planeId}")
          Left(PlaneNotFound)
        case _ => logger.error(s"[updatePlane] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

  def removePlane(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    wsClient.url(s"$baseUrl/planes/remove/$planeId").delete().map { response =>
      response.status match {
        case OK => response.json.validate[Plane].fold(
          invalid => {
            logger.error(s"[removePlane] Json could not be parsed from the backend. Status = ${response.status}, invalid json = $invalid")
            Left(JsonParseError)
          },
          valid => Right(valid)
        )
        case NOT_FOUND => logger.warn(s"[removePlane] Plane was not found in the backend. Plane Id: $planeId")
          Left(PlaneNotFound)
        case _ => logger.error(s"[removePlane] Unexpected status returned from the backend. Status = ${response.status}")
          Left(UnexpectedStatus)
      }
    }
  }

}
