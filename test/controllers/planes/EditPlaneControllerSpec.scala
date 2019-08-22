package controllers.planes

import forms.PlaneForm
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, ElementNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.EditPlane

import scala.concurrent.Future


class EditPlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockEditPlane: EditPlane = mock[EditPlane]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val fakeRequestWithSession: FakeRequest[AnyContent] = FakeRequest().withSession(
      "journey" -> s"${campaign.id}"
    )

    val fakeRequestWithSessionAndForm: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequestWithSession.withFormUrlEncodedBody(
      PlaneForm.planeName -> "updatedPlaneName",
      PlaneForm.planeDescription -> planeDescription,
      PlaneForm.planeAlignment -> planeAlignment
    )

    val controller: EditPlaneController = new EditPlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val editPlane: EditPlane = mockEditPlane
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "the plane is retrieved from the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockEditPlane(matches(plane.id), any())) thenReturn emptyHtmlTag

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
      "there was an error when retrieving the plane from the campaign" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.show(plane.id)(fakeRequestWithSession)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form had errors" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockEditPlane(matches(plane.id), any())) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSession)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $NOT_FOUND" when {
      "finding the element to edit and the campaign could not be found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "finding the element to edit and the element could not be found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(ElementNotFound))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
      "updating the element and the campaign could not be found" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockCampaignService.replaceElement(matches(campaign.id), matches(plane.copy(name = "updatedPlaneName")))(any()))
          .thenReturn(Future.successful(Left(CampaignNotFound)))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an error occurred when retrieving the element" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "an error occured when updating the element" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockCampaignService.replaceElement(matches(campaign.id), matches(plane.copy(name = "updatedPlaneName")))(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $SEE_OTHER" when {
      "the element was replaced" in new Setup {
        when(mockCampaignService.retrieveElement(matches(campaign.id), matches(plane.id))(any())) thenReturn Future.successful(Right(plane))
        when(mockCampaignService.replaceElement(matches(campaign.id), matches(plane.copy(name = "updatedPlaneName")))(any()))
          .thenReturn(Future.successful(Right(plane.copy(name = "updatedPlaneName"))))

        val result: Future[Result] = controller.submit(plane.id)(fakeRequestWithSessionAndForm)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
        session(result).get("journey").map(_.split(',').length) mustBe Some(2)
      }
    }
  }

}
