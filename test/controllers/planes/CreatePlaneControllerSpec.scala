package controllers.planes

import config.AppConfig
import forms.PlaneForm
import helpers.UnitSpec
import models.Plane
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.CampaignService
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.CreatePlane

import scala.concurrent.Future

class CreatePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockCreatePlane: CreatePlane = mock[CreatePlane]
    val mockNotFound: NotFound = mock[NotFound]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]

    val fakeRequest: FakeRequest[AnyContent] = FakeRequest()
    val fakeRequestWithSessionAndForm: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withSession("journey" -> campaign.id).withFormUrlEncodedBody(
      PlaneForm.planeName -> planeName,
      PlaneForm.planeDescription -> planeDescription,
      PlaneForm.planeAlignment -> planeAlignment
    )

    val controller: CreatePlaneController = new CreatePlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val createPlane: CreatePlane = mockCreatePlane
      val notFound: NotFound = mockNotFound
      val internalServerError: InternalServerError = mockInternalServerError
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreatePlane(any())) thenReturn emptyHtmlTag

      val result: Future[Result] = controller.show()(fakeRequest)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form has errors" in new Setup {
        when(mockCreatePlane(any())) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit()(fakeRequest)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the campaign is updated with a plane" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Plane])(any()))
          .thenReturn(Future.successful(Right(campaign.copy(content = List(plane)))))

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
        session(result).get("journey").map(_.split(',').length) mustBe Some(2)
      }
    }
    s"return $NOT_FOUND" when {
      "the campaign to add the element to could not be found" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Plane])(any()))
          .thenReturn(Future.successful(Left(CampaignNotFound)))
        when(mockNotFound()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was an error when adding the element to the campaign" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Plane])(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))
        when(mockInternalServerError()) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
