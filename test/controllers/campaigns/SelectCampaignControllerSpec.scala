package controllers.campaigns

import helpers.UnitSpec
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import utils.ErrorModel.UnexpectedStatus
import utils.TestConstants
import views.campaigns.SelectCampaign
import views.errors.InternalServerError

import scala.concurrent.Future

class SelectCampaignControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockSelectCampaign: SelectCampaign = mock[SelectCampaign]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val controller: SelectCampaignController = new SelectCampaignController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val selectCampaign: SelectCampaign = mockSelectCampaign
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK and remove the journey key" when {
      "campaigns are returned from the service" in new Setup {
        when(mockCampaignService.retrieveAllCampaigns(any())) thenReturn Future.successful(Right(List(campaign, campaign)))
        when(mockSelectCampaign(List(campaign, campaign))) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(FakeRequest().withSession("journey" -> "testJourney"))
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
        session(result).data mustBe Map.empty[String, String]
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an error is returned back from the service" in new Setup {
        when(mockCampaignService.retrieveAllCampaigns(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "view" must {
    s"return $SEE_OTHER and set the journey key to the passed in id" in new Setup {
      val result: Future[Result] = controller.view("testId")(FakeRequest())
      status(result) mustBe SEE_OTHER
      session(result).get("journey") mustBe Some("testId")
    }
  }

}
