package controllers.planes

import helpers.UnitSpec
import models.ErrorModel.{CampaignNotFound, ElementNotFound, UnexpectedStatus}
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import testutil.TestConstants
import views.planes.DeletePlane

import scala.concurrent.Future

class DeletePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockDeletePlane: DeletePlane = mock[DeletePlane]
    val mockCampaignService: CampaignService = mock[CampaignService]

    val fakeRequestWithSession: FakeRequest[AnyContent] = FakeRequest().withSession(
      "journey" -> s"${campaign.id},${plane.id}"
    )

    val controller: DeletePlaneController = new DeletePlaneController {
      val controllerComponents: MessagesControllerComponents = stubMessagesControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val deletePlane: DeletePlane = mockDeletePlane
    }

  }

  "show" must {
    s"return $OK" when {
      "the plane is retrieved from the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockDeletePlane(matches(plane))(any())) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "the campaign was not found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
      }
      "the plane was not found in the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(ElementNotFound))

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem retrieving the element" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  "submit" must {
    s"return $SEE_OTHER" when {
      "the element is removed from the campaign" in new Setup {
        when(mockCampaignService.removeElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
      }
    }

    s"return $NOT_FOUND" when {
      "the campaign to remove from was not found" in new Setup {
        when(mockCampaignService.removeElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem when removing the plane" in new Setup {
        when(mockCampaignService.removeElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
