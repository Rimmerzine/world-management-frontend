package controllers

import helpers.UnitSpec
import models.ErrorModel.{CampaignNotFound, ElementNotFound, UnexpectedStatus}
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import testutil.TestConstants
import views.SelectElement

import scala.concurrent.Future

class SelectControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockSelectElement: SelectElement = mock[SelectElement]

    val fakeRequestWithSession: FakeRequest[AnyContent] = FakeRequest().withSession(
      "journey" -> s"${campaign.id},${plane.id}"
    )

    val controller: SelectController = new SelectController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val selectElement: SelectElement = mockSelectElement
    }

  }

  "show" must {
    s"return $OK" when {
      "the element to show is retrieved" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockSelectElement(matches(campaign.id), matches(plane))) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show()(fakeRequestWithSession)

        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "the campaign could not be found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))

        val result: Future[Result] = controller.show()(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
      }
      "the element could not be found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(ElementNotFound))

        val result: Future[Result] = controller.show()(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem retrieving the element" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        val result: Future[Result] = controller.show()(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  "view" must {
    s"redirect to the select controller, adding the new id to the journey" in new Setup {
      val result: Future[Result] = controller.view("testId")(fakeRequestWithSession)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
      session(result).get("journey") mustBe Some(s"${campaign.id},${plane.id},testId")
    }
  }

  "back" must {
    s"redirect to the select controller, removing the last item on the journey" in new Setup {
      val result: Future[Result] = controller.back()(fakeRequestWithSession)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
      session(result).get("journey") mustBe Some(s"${campaign.id}")
    }
  }

}
