package services

import connectors.LandConnector
import javax.inject.Inject
import models.Land
import utils.ErrorModel

import scala.concurrent.{ExecutionContext, Future}

class LandServiceImpl @Inject()(val landConnector: LandConnector) extends LandService

trait LandService {

  val landConnector: LandConnector

  def retrieveAllLands(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Land]]] = {
    landConnector.retrieveAllLands(planeId)
  }

  def retrieveSingleLand(landId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    landConnector.retrieveSingleLand(landId)
  }

  def createLand(land: Land)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    landConnector.createLand(land)
  }

  def updateLand(land: Land)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    landConnector.updateLand(land)
  }

  def removeLand(landId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Land]] = {
    landConnector.removeLand(landId)
  }

}
