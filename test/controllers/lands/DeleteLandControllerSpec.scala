package controllers.lands

import config.AppConfig
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import services.LandService
import utils.ErrorModel.{LandNotFound, UnexpectedStatus}
import utils.TestConstants
import views.errors.{InternalServerError, NotFound}
import views.lands.DeleteLand

import scala.concurrent.Future

class DeleteLandControllerSpec extends UnitSpec with TestConstants {

  trait Setup {

    val mockLandService: LandService = mock[LandService]
    val mockAppConfig: AppConfig = mock[AppConfig]
    val mockDeleteLand: DeleteLand = mock[DeleteLand]
    val mockInternalServerError: InternalServerError = mock[InternalServerError]
    val mockNotFound: NotFound = mock[NotFound]

    val controller: DeleteLandController = new DeleteLandController {
      val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
      val landService: LandService = mockLandService
      implicit val appConfig: AppConfig = mockAppConfig
      val deleteLand: DeleteLand = mockDeleteLand
      val internalServerError: InternalServerError = mockInternalServerError
      val notFound: NotFound = mockNotFound
    }

  }

  "show" must {
    s"return $OK" when {
      "a land is returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))
        when(mockDeleteLand(matches(testLand))) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $NOT_FOUND" when {
      "a LandNotFound is returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(LandNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockLandService.retrieveSingleLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.show(testLand.landId)(FakeRequest())
        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
  }

  "submit" must {
    s"return $NOT_FOUND" when {
      "LandNotFound was returned from the service" in new Setup {
        when(mockLandService.removeLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(LandNotFound))
        when(mockNotFound()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testLand.landId)(FakeRequest())

        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "any other error is returned from the service" in new Setup {
        when(mockLandService.removeLand(matches(testLand.landId))(any())) thenReturn Future.successful(Left(UnexpectedStatus))
        when(mockInternalServerError()) thenReturn emptyHtml

        val result: Future[Result] = controller.submit(testLand.landId)(FakeRequest())

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $SEE_OTHER" when {
      "the land was created" in new Setup {
        when(mockLandService.removeLand(matches(testLand.landId))(any())) thenReturn Future.successful(Right(testLand))

        val result: Future[Result] = controller.submit(testLand.landId)(FakeRequest())

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.lands.routes.SelectLandController.show(testLand.planeId).url)
      }
    }
  }

}
