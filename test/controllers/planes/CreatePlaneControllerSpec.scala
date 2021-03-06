package controllers.planes

import config.AppConfig
import forms.PlaneForm
import helpers.UnitSpec
import models.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import models.Plane
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, AnyContentAsFormUrlEncoded, MessagesControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import testutil.TestConstants
import views.planes.CreatePlane

import scala.concurrent.Future

class CreatePlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockCreatePlane: CreatePlane = mock[CreatePlane]

    val fakeRequest: FakeRequest[AnyContent] = FakeRequest()
    val fakeRequestWithSessionAndForm: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withSession("journey" -> campaign.id).withFormUrlEncodedBody(
      PlaneForm.planeName -> planeName,
      PlaneForm.planeDescription -> planeDescription,
      PlaneForm.planeAlignment -> planeAlignment
    )

    val controller: CreatePlaneController = new CreatePlaneController {
      val controllerComponents: MessagesControllerComponents = stubMessagesControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val createPlane: CreatePlane = mockCreatePlane
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreatePlane(any())(any())) thenReturn emptyHtmlTag

      val result: Future[Result] = controller.show()(fakeRequest)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form has errors" in new Setup {
        when(mockCreatePlane(any())(any())) thenReturn emptyHtmlTag

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

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was an error when adding the element to the campaign" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Plane])(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

}
