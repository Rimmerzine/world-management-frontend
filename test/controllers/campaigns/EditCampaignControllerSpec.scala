package controllers.campaigns

import forms.CampaignForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.campaigns.EditCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.Future

class EditCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockEditCampaign: EditCampaign = mock[EditCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: EditCampaignController = new EditCampaignController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val editCampaign: EditCampaign = mockEditCampaign
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a campaign was returned from the service" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockEditCampaign(any(), matches(campaign.id))) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(campaign.id)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "CampaignNotFound was returned from the service" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(campaign.id)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(campaign.id)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $SEE_OTHER" when {
      "the campaign was updated" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockCampaignService.updateCampaign(matches(campaign.copy(name = "updatedCampaignName")))(any()))
          .thenReturn(Future.successful(Right(campaign.copy(name = "updatedCampaignName"))))

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> "updatedCampaignName",
          CampaignForm.campaignDescription -> campaignDescription
        )
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe SEE_OTHER
        session(result).get("journey").map(_.split(',').length) mustBe Some(1)
      }
    }

    s"return $NOT_FOUND" when {
      "the campaign was not found when retrieved" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> "updatedCampaignName",
          CampaignForm.campaignDescription -> campaignDescription
        )
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "the campaign was not found when updating" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockCampaignService.updateCampaign(matches(campaign.copy(name = "updatedCampaignName")))(any()))
          .thenReturn(Future.successful(Left(CampaignNotFound)))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> "updatedCampaignName",
          CampaignForm.campaignDescription -> campaignDescription
        )
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "there was an error when retrieving the campaign" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> "updatedCampaignName",
          CampaignForm.campaignDescription -> campaignDescription
        )
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "there was an error when updating the campaign" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockCampaignService.updateCampaign(matches(campaign.copy(name = "updatedCampaignName")))(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(
          CampaignForm.campaignName -> "updatedCampaignName",
          CampaignForm.campaignDescription -> campaignDescription
        )
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockEditCampaign(any(), matches(campaign.id))) thenReturn emptyHtmlTag

        val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody()
        val result: Future[Result] = controller.submit(campaign.id)(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
