package services

import connectors.PlaneConnector
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PlaneServiceSpec extends UnitSpec with TestConstants {

  class Setup {
    val mockPlaneConnector: PlaneConnector = mock[PlaneConnector]

    val service: PlaneService = new PlaneService {
      val planeConnector: PlaneConnector = mockPlaneConnector
    }
  }

  "retrieveAllPlanes" must {
    "return back what it receives from the connector" in new Setup {
      when(mockPlaneConnector.retrieveAllPlanes(matches(testCampaignId))(any())) thenReturn Future.successful(Right(List(testPlane, testPlaneMinimal)))

      await(service.retrieveAllPlanes(testCampaignId)) mustBe Right(List(testPlane, testPlaneMinimal))
    }
  }

  "retrieveSinglePlane" must {
    "return back what it receives from the connector" in new Setup {
      when(mockPlaneConnector.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))

      await(service.retrieveSinglePlane(testPlane.planeId)) mustBe Right(testPlane)
    }
  }

  "createPlane" must {
    "return back what it receives from the connector" in new Setup {
      when(mockPlaneConnector.createPlane(matches(testPlane))(any())) thenReturn Future.successful(Right(testPlane))

      await(service.createPlane(testPlane)) mustBe Right(testPlane)
    }
  }

  "updatePlane" must {
    "return back what it receives from the connector" in new Setup {
      when(mockPlaneConnector.updatePlane(matches(testPlane))(any())) thenReturn Future.successful(Right(testPlane))

      await(service.updatePlane(testPlane)) mustBe Right(testPlane)
    }
  }

  "removePlane" must {
    "return back what it receives from the connector" in new Setup {
      when(mockPlaneConnector.removePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))

      await(service.removePlane(testPlane.planeId)) mustBe Right(testPlane)
    }
  }

}
