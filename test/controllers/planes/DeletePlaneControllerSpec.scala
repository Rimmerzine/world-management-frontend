package controllers.planes

import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, ElementNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.DeletePlane

import scala.concurrent.Future

class DeletePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockDeletePlane: DeletePlane = mock[DeletePlane]
    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val fakeRequestWithSession: FakeRequest[AnyContent] = FakeRequest().withSession(
      "journey" -> s"${campaign.id},${plane.id}"
    )

    val controller: DeletePlaneController = new DeletePlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val deletePlane: DeletePlane = mockDeletePlane
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "the plane is retrieved from the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockDeletePlane(plane)) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "the campaign was not found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "the plane was not found in the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(ElementNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem retrieving the element" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
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
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "there was a problem when removing the plane" in new Setup {
        when(mockCampaignService.removeElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }
}
