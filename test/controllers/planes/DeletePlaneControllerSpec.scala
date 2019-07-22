package controllers.planes

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.PlaneService
import utils.ErrorModel.{PlaneNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.DeletePlane

import scala.concurrent.Future

class DeletePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockPlaneService: PlaneService = mock[PlaneService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockDeletePlane: DeletePlane = mock[DeletePlane]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: DeletePlaneController = new DeletePlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val planeService: PlaneService = mockPlaneService
      implicit val appConfig: AppConfig = mockAppConfig
      val deletePlane: DeletePlane = mockDeletePlane
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a plane is returned from the service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockDeletePlane(matches(testPlane))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlane.planeId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "a PlaneNotFound is returned from the service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(PlaneNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlane.planeId)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlane.planeId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $NOT_FOUND" when {
      "PlaneNotFound was returned from the service" in new Setup {
        when(mockPlaneService.removePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(PlaneNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testPlane.planeId)(FakeRequest())

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockPlaneService.removePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testPlane.planeId)(FakeRequest())

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the plane was created" in new Setup {
        when(mockPlaneService.removePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))

        val result: Future[Result] = controller.submit(testPlane.planeId)(FakeRequest())

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.planes.routes.SelectPlaneController.show(testCampaignId).url)
      }
    }
  }

}
