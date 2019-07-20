package controllers

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.campaigns.DeleteCampaign
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.Future

class DeleteCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockDeleteCampaign: DeleteCampaign = mock[DeleteCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: DeleteCampaignController = new DeleteCampaignController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      implicit val appConfig: AppConfig = mockAppConfig
      val deleteCampaign: DeleteCampaign = mockDeleteCampaign
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a campaign is returned from the service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Right(testCampaign))
        when(mockDeleteCampaign.apply(matches(testCampaign))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaign.id)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "a CampaignNotFound is returned from the service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound.apply()) thenReturn emptyHtml

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
    s"return $NOT_FOUND" when {
      "CampaignNotFound was returned from the service" in new Setup {
        when(mockCampaignService.removeCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testCampaign.id)(FakeRequest())

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockCampaignService.removeCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testCampaign.id)(FakeRequest())

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the campaign was created" in new Setup {
        when(mockCampaignService.removeCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Right(testCampaign))

        val result: Future[Result] = controller.submit(testCampaign.id)(FakeRequest())

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectCampaignController.show().url)
      }
    }
  }

}
