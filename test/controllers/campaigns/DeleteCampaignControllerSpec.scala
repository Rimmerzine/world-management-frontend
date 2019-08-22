package controllers.campaigns

import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.campaigns.DeleteCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.Future

class DeleteCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockDeleteCampaign: DeleteCampaign = mock[DeleteCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: DeleteCampaignController = new DeleteCampaignController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val deleteCampaign: DeleteCampaign = mockDeleteCampaign
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a campaign is returned from the service" in new Setup {
        when(mockCampaignService.retrieveCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockDeleteCampaign(matches(campaign))) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(campaign.id)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "a CampaignNotFound is returned from the service" in new Setup {
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
      "the campaign was deleted" in new Setup {
        when(mockCampaignService.removeCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))

        val result: Future[Result] = controller.submit(campaign.id)(FakeRequest())

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.campaigns.routes.SelectCampaignController.show().url)
      }
    }
    s"return $NOT_FOUND" when {
      "CampaignNotFound was returned from the service" in new Setup {
        when(mockCampaignService.removeCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(campaign.id)(FakeRequest())

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockCampaignService.removeCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(campaign.id)(FakeRequest())

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
