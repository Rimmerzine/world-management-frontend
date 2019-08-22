package controllers

import helpers.UnitSpec
import play.api.http.Status
import play.api.mvc.{AnyContent, AnyContentAsEmpty, ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.mvc.Results.Ok
import play.api.test.FakeRequest

import scala.concurrent.Future

class FrontendControllerSpec extends UnitSpec {

  trait Setup {
    val controller: FrontendController = new FrontendController {
      val controllerComponents: ControllerComponents = stubControllerComponents()
    }

    val returnValue: (String, List[String]) => Future[Result] = {
      case (campaignId, journey) => Future.successful(Ok(s"$campaignId,${journey.mkString(",")}"))
    }

    val journeyString = "campaignId,planeId"

    val fakeRequestWithJourney: FakeRequest[AnyContent] = FakeRequest().withSession("journey" -> journeyString)
  }

  "withNavCollection" must {
    "execute the provided function" when {
      "there is a journey in session with a campaign id" in new Setup {
        val result: Future[Result] = controller.withNavCollection(returnValue)(fakeRequestWithJourney)

        status(result) mustBe OK
        contentAsString(result) mustBe s"campaignId,$journeyString"
      }
    }
    "redirect to the home page" when {
      "journey is not in session" in new Setup {
        val result: Future[Result] = controller.withNavCollection(returnValue)(FakeRequest())

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.campaigns.routes.SelectCampaignController.show().url)
      }
      "journey is in session but there is no value" in new Setup {
        val result: Future[Result] = controller.withNavCollection(returnValue)(FakeRequest().withSession("journey" -> ""))

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.campaigns.routes.SelectCampaignController.show().url)
      }
    }
  }
}
