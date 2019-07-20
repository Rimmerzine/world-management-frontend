package controllers

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.UnexpectedStatus
import utils.TestConstants
import views.campaigns.SelectCampaign
import views.errors.InternalServerError

import scala.concurrent.Future

class SelectCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockSelectCampaign: SelectCampaign = mock[SelectCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val controller: SelectCampaignController = new SelectCampaignController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      implicit val appConfig: AppConfig = mockAppConfig
      val selectCampaign: SelectCampaign = mockSelectCampaign
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK" when {
      "campaigns are returned from the service" in new Setup {
        when(mockCampaignService.retrieveAllCampaigns(any())) thenReturn Future.successful(Right(List(testCampaign, testCampaignMinimal)))
        when(mockSelectCampaign(List(testCampaign, testCampaignMinimal))) thenReturn emptyHtml

        val result: Future[Result] = controller.show()(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an error is returned back from the service" in new Setup {
        when(mockCampaignService.retrieveAllCampaigns(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show()(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
