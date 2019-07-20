package controllers

import config.AppConfig
import forms.CampaignForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.EditCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.Future

class EditCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockEditCampaign: EditCampaign = mock[EditCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: EditCampaignController = new EditCampaignController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      implicit val appConfig: AppConfig = mockAppConfig
      val editCampaign: EditCampaign = mockEditCampaign
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a campaign was returned from the service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Right(testCampaign))
        when(mockEditCampaign(any(), matches(testCampaign.id))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaign.id)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "CampaignNotFound was returned from the service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaign.id)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaign.id)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockEditCampaign(any(), matches(testCampaign.id))) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(testCampaign.id)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "CampaignNotFound is returned from the service" in new Setup {
        when(mockCampaignService.updateCampaign(matches(testCampaign))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> testCampaignName,
          CampaignForm.campaignDescription -> testCampaignDescription
        )
        val result: Future[Result] = controller.submit(testCampaign.id)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockCampaignService.updateCampaign(matches(testCampaign))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> testCampaignName,
          CampaignForm.campaignDescription -> testCampaignDescription
        )
        val result: Future[Result] = controller.submit(testCampaign.id)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the campaign was created" in new Setup {
        when(mockCampaignService.updateCampaign(matches(testCampaign))(any())) thenReturn Future.successful(Right(testCampaign))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> testCampaignName,
          CampaignForm.campaignDescription -> testCampaignDescription
        )
        val result: Future[Result] = controller.submit(testCampaign.id)(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectCampaignController.show().url)
      }
    }
  }

}
