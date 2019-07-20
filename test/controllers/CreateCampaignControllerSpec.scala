package controllers

import config.AppConfig
import forms.CampaignForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.UnexpectedStatus
import utils.TestConstants
import views.CreateCampaign
import views.errors.InternalServerError

import scala.concurrent.Future

class CreateCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockCreateCampaign: CreateCampaign = mock[CreateCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val controller: CreateCampaignController = new CreateCampaignController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      implicit val appConfig: AppConfig = mockAppConfig
      val createCampaign: CreateCampaign = mockCreateCampaign
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreateCampaign(any())) thenReturn "<html></html>"

      val result: Future[Result] = controller.show()(FakeRequest())
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockCreateCampaign(any())) thenReturn "<html></html>"

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit()(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem creating a campaign" in new Setup {
        when(mockCampaignService.createCampaign(any())(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn "<html></html>"

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> testCampaignName,
          CampaignForm.campaignDescription -> testCampaignDescription
        )
        val result: Future[Result] = controller.submit()(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the campaign was created" in new Setup {
        when(mockCampaignService.createCampaign(any())(any())) thenReturn Future.successful(Right(testCampaign))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> testCampaignName,
          CampaignForm.campaignDescription -> testCampaignDescription
        )
        val result: Future[Result] = controller.submit()(request)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectCampaignController.show().url)
      }
    }
  }

}
