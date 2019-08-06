package controllers.lands

import config.AppConfig
import forms.LandForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.LandService
import utils.ErrorModel.{LandNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.lands.EditLand

import scala.concurrent.Future

class EditLandControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockLandService: LandService = mock[LandService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockEditLand: EditLand = mock[EditLand]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: EditLandController = new EditLandController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val landService: LandService = mockLandService
      implicit val appConfig: AppConfig = mockAppConfig
      val editLand: EditLand = mockEditLand
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a land was returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockEditLand(matches(testLand.landId), any())) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "LandNotFound was returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(LandNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockEditLand(matches(testLand.landId), any())) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "LandNotFound is returned from the service retrieve land call" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(LandNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "LandNotFound is returned from the service update land call" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockLandService.updateLand(matches(testLand))(any())) thenReturn Future.successful(Left(LandNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service retrieve land call" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "any other error is returned from the service update land call" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockLandService.updateLand(matches(testLand))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the land was updated" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockLandService.updateLand(matches(testLand))(any())) thenReturn Future.successful(Right(testLand))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )
        val result: Future[Result] = controller.submit(testLand.landId)(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.lands.routes.SelectLandController.show(testLand.planeId).url)
      }
    }
  }

}
