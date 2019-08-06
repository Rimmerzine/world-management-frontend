package controllers.planes

import config.AppConfig
import forms.PlaneForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.PlaneService
import utils.ErrorModel.{PlaneNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.EditPlane

import scala.concurrent.Future

class EditPlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockPlaneService: PlaneService = mock[PlaneService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockEditPlane: EditPlane = mock[EditPlane]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: EditPlaneController = new EditPlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val planeService: PlaneService = mockPlaneService
      implicit val appConfig: AppConfig = mockAppConfig
      val editPlane: EditPlane = mockEditPlane
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a plane was returned from the service" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockEditPlane(matches(testPlane.planeId), any())) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testPlane.planeId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "PlaneNotFound was returned from the service" in new Setup {
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
    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockEditPlane(matches(testPlane.planeId), any())) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "PlaneNotFound is returned from the service retrieve plane call" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(PlaneNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "PlaneNotFound is returned from the service update plane call" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockPlaneService.updatePlane(matches(testPlane))(any())) thenReturn Future.successful(Left(PlaneNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service retrieve plane call" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "any other error is returned from the service update plane call" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockPlaneService.updatePlane(matches(testPlane))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the plane was updated" in new Setup {
        when(mockPlaneService.retrieveSinglePlane(matches(testPlane.planeId))(any())) thenReturn Future.successful(Right(testPlane))
        when(mockPlaneService.updatePlane(matches(testPlane))(any())) thenReturn Future.successful(Right(testPlane))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )
        val result: Future[Result] = controller.submit(testPlane.planeId)(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.lands.routes.SelectLandController.show(testLand.planeId).url)
      }
    }
  }

}
