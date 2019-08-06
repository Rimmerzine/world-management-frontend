package controllers.planes

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.{CampaignService, PlaneService}
import utils.ErrorModel.{CampaignNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.planes.SelectPlane

import scala.concurrent.Future

class SelectPlaneControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockCampaignService: CampaignService = mock[CampaignService]
    val mockPlaneService: PlaneService = mock[PlaneService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockSelectPlane: SelectPlane = mock[SelectPlane]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: SelectPlaneController = new SelectPlaneController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val campaignService: CampaignService = mockCampaignService
      val planeService: PlaneService = mockPlaneService
      implicit val appConfig: AppConfig = mockAppConfig
      val selectPlane: SelectPlane = mockSelectPlane
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "planes are returned from the plane service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaignId))(any())) thenReturn Future.successful(Right(testCampaign))
        when(mockPlaneService.retrieveAllPlanes(matches(testCampaignId))(any())) thenReturn Future.successful(Right(List(testPlane, testPlaneMinimal)))
        when(mockSelectPlane(testCampaign, List(testPlane, testPlaneMinimal))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaignId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $NOT_FOUND" when {
      "the campaign service returns CampaignNotFound" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaignId))(any())) thenReturn Future.successful(Left(CampaignNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaignId)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an error is returned back from the campaign service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaignId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaignId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
      "an error is returned back from the plane service" in new Setup {
        when(mockCampaignService.retrieveSingleCampaign(matches(testCampaignId))(any())) thenReturn Future.successful(Right(testCampaign))
        when(mockPlaneService.retrieveAllPlanes(matches(testCampaignId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testCampaignId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
