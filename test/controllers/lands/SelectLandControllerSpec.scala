package controllers.lands

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.{LandService, PlaneService}
import utils.ErrorModel.{PlaneNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.lands.SelectLand

import scala.concurrent.Future

class SelectLandControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockPlaneService: PlaneService = mock[PlaneService]
    val mockLandService: LandService = mock[LandService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockSelectLand: SelectLand = mock[SelectLand]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: SelectLandController = new SelectLandController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val planeService: PlaneService = mockPlaneService
      val landService: LandService = mockLandService
      implicit val appConfig: AppConfig = mockAppConfig
      val selectLand: SelectLand = mockSelectLand
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "lands are returned from the land service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlaneId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockLandService.retrieveAllLands(matches(testPlaneId))(any())) thenReturn Future.successful(Right(List(testLand, testLandMinimal)))
        when(mockSelectLand(testPlane, List(testLand, testLandMinimal))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlaneId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $NOT_FOUND" when {
      "the plane service returns PlaneNotFound" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlaneId))(any())) thenReturn Future.successful(Left(PlaneNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlaneId)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an error is returned back from the plane service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlaneId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlaneId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "an error is returned back from the land service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlaneId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockLandService.retrieveAllLands(matches(testPlaneId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlaneId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
