package controllers.lands

import forms.LandForm
import helpers.UnitSpec
import models.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import models.Land
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{AnyContent, AnyContentAsFormUrlEncoded, ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CampaignService
import testutil.TestConstants
import views.lands.CreateLand

import scala.concurrent.Future

class CreateLandControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockCreateLand: CreateLand = mock[CreateLand]

    val fakeRequest: FakeRequest[AnyContent] = FakeRequest()
    val fakeRequestWithSessionAndForm: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withSession("journey" -> campaign.id).withFormUrlEncodedBody(
      LandForm.landName -> landName,
      LandForm.landDescription -> landDescription
    )

    val controller: CreateLandController = new CreateLandController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val createLand: CreateLand = mockCreateLand
    }

  }

  "show" must {
    s"return $OK" in new Setup {
      when(mockCreateLand(any())) thenReturn emptyHtmlTag

      val result: Future[Result] = controller.show()(fakeRequest)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
    }
  }

  "submit" must {
    s"return $BAD_REQUEST" when {
      "the form has errors" in new Setup {
        when(mockCreateLand(any())) thenReturn emptyHtmlTag

        val result: Future[Result] = controller.submit()(fakeRequest)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the campaign is updated with a land" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Land])(any()))
          .thenReturn(Future.successful(Right(campaign.copy(content = List(land)))))

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.SelectController.show().url)
        session(result).get("journey").map(_.split(',').length) mustBe Some(2)
      }
    }
    s"return $NOT_FOUND" when {
      "the campaign to add the element to could not be found" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Land])(any()))
          .thenReturn(Future.successful(Left(CampaignNotFound)))

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe NOT_FOUND
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "there was an error when adding the element to the campaign" in new Setup {
        when(mockCampaignService.addElement(matches(campaign.id), matches(campaign.id), any[Land])(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))

        val result: Future[Result] = controller.submit()(fakeRequestWithSessionAndForm)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

}
