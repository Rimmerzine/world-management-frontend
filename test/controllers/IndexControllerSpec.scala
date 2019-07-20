package controllers

import helpers.UnitSpec
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.Future

class IndexControllerSpec extends UnitSpec {

  trait Setup {
    val controller: IndexController = new IndexController() {
      override protected def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }
  }

  "index" must {
    "redirect the user to the home page" in new Setup {
      val result: Future[Result] = controller.index()(FakeRequest())
      status(result) mustBe 303
      redirectLocation(result) mustBe Some(controllers.routes.SelectCampaignController.show().url)
    }
  }
}
