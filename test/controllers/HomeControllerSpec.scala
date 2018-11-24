package controllers

import helpers.UnitSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.Future

class HomeControllerSpec extends UnitSpec {

  trait Setup {
    val controller: HomeController = new HomeController() {
      override protected def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }
  }

  "show" must {
    "return ok with html to the user for the home page" in new Setup {
      val result: Future[Result] = controller.show()(FakeRequest())
      status(result) mustBe 200
    }
  }

  "start" must {
    "redirect the user to the contents page" in new Setup {
      val result: Future[Result] = controller.start()(FakeRequest())
      status(result) mustBe 303
      redirectLocation(result) mustBe Some("/start")
    }
  }
}
