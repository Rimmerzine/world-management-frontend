package controllers

import helpers.UnitSpec
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class IndexControllerSpec extends UnitSpec {

  trait Setup {
    val controller: IndexController = new IndexController() {
      val controllerComponents: ControllerComponents = stubControllerComponents()
    }
  }

  "index" must {
    "redirect the user to the home page" in new Setup {
      val result: Future[Result] = controller.index()(FakeRequest())
      status(result) mustBe 303
      redirectLocation(result) mustBe Some(controllers.campaigns.routes.SelectCampaignController.show().url)
    }
  }
}
