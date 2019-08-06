package controllers.lands

import config.AppConfig
import forms.LandForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.LandService
import utils.ErrorModel.UnexpectedStatus
import utils.TestConstants
import views.errors.InternalServerError
import views.lands.CreateLand

import scala.concurrent.Future

class CreateLandControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockLandService: LandService = mock[LandService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockCreateLand: CreateLand = mock[CreateLand]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val controller: CreateLandController = new CreateLandController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val landService: LandService = mockLandService
      implicit val appConfig: AppConfig = mockAppConfig
      val createLand: CreateLand = mockCreateLand
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreateLand(matches(testPlaneId), any())) thenReturn emptyHtml

      val result: Future[Result] = controller.show(testPlaneId)(FakeRequest())
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form has errors" in new Setup {
        when(mockCreateLand(matches(testPlaneId), any())) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(testPlaneId)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem creating a land" in new Setup {
        when(mockLandService.createLand(any())(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )
        val result: Future[Result] = controller.submit(testPlaneId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the land was created" in new Setup {
        when(mockLandService.createLand(any())(any()))
          .thenReturn(Future.successful(Right(testLand)))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )
        val result: Future[Result] = controller.submit(testPlaneId)(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.lands.routes.SelectLandController.show(testLand.planeId).url)
      }
    }
  }

}
