package services

import connectors.PlaneConnector
import javax.inject.Inject
import models.Plane
import utils.ErrorModel

import scala.concurrent.{ExecutionContext, Future}

class PlaneServiceImpl @Inject()(val planeConnector: PlaneConnector) extends PlaneService

trait PlaneService {

  val planeConnector: PlaneConnector

  def retrieveAllPlanes(campaignId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, List[Plane]]] = {
    planeConnector.retrieveAllPlanes(campaignId)
  }

  def retrieveSinglePlane(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    planeConnector.retrieveSinglePlane(planeId)
  }

  def createPlane(plane: Plane)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    planeConnector.createPlane(plane)
  }

  def updatePlane(plane: Plane)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    planeConnector.updatePlane(plane)
  }

  def removePlane(planeId: String)(implicit ec: ExecutionContext): Future[Either[ErrorModel, Plane]] = {
    planeConnector.removePlane(planeId)
  }

}
