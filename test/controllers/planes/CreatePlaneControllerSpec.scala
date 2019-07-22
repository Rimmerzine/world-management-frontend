package controllers.planes

import config.AppConfig
import forms.PlaneForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.PlaneService
import utils.ErrorModel.UnexpectedStatus
import utils.TestConstants
import views.errors.InternalServerError
import views.planes.CreatePlane

import scala.concurrent.Future

class CreatePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockPlaneService: PlaneService = mock[PlaneService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockCreatePlane: CreatePlane = mock[CreatePlane]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val controller: CreatePlaneController = new CreatePlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val planeService: PlaneService = mockPlaneService
      implicit val appConfig: AppConfig = mockAppConfig
      val createPlane: CreatePlane = mockCreatePlane
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreatePlane(matches(testCampaignId), any())) thenReturn "<html></html>"

      val result: Future[Result] = controller.show(testCampaignId)(FakeRequest())
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form has errors" in new Setup {
        when(mockCreatePlane(matches(testCampaignId), any())) thenReturn "<html></html>"

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(testCampaignId)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem creating a plane" in new Setup {
        when(mockPlaneService.createPlane(any())(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn "<html></html>"

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )
        val result: Future[Result] = controller.submit(testCampaignId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the plane was created" in new Setup {
        when(mockPlaneService.createPlane(any())(any()))
          .thenReturn(Future.successful(Right(testPlane)))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )
        val result: Future[Result] = controller.submit(testCampaignId)(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.planes.routes.SelectPlaneController.show(testCampaignId).url)
      }
    }
  }

}
