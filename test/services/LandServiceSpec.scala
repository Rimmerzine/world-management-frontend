package services

import connectors.LandConnector
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LandServiceSpec extends UnitSpec with TestConstants {

  class Setup {
    val mockLandConnector: LandConnector = mock[LandConnector]

    val service: LandService = new LandService {
      val landConnector: LandConnector = mockLandConnector
    }
  }

  "retrieveAllLands" must {
    "return back what it receives from the connector" in new Setup {
      when(mockLandConnector.retrieveAllLands(matches(testPlaneId))(any())) thenReturn Future.successful(Right(List(testLand, testLandMinimal)))

      await(service.retrieveAllLands(testPlaneId)) mustBe Right(List(testLand, testLandMinimal))
    }
  }

  "retrieveSingleLand" must {
    "return back what it receives from the connector" in new Setup {
      when(mockLandConnector.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))

      await(service.retrieveSingleLand(testLand.landId)) mustBe Right(testLand)
    }
  }

  "createLand" must {
    "return back what it receives from the connector" in new Setup {
      when(mockLandConnector.createLand(matches(testLand))(any())) thenReturn Future.successful(Right(testLand))

      await(service.createLand(testLand)) mustBe Right(testLand)
    }
  }

  "updateLand" must {
    "return back what it receives from the connector" in new Setup {
      when(mockLandConnector.updateLand(matches(testLand))(any())) thenReturn Future.successful(Right(testLand))

      await(service.updateLand(testLand)) mustBe Right(testLand)
    }
  }

  "removeLand" must {
    "return back what it receives from the connector" in new Setup {
      when(mockLandConnector.removeLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))

      await(service.removeLand(testLand.landId)) mustBe Right(testLand)
    }
  }

}
