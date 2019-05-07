package views

import helpers.ViewSpec
import play.twirl.api.Html

class HomeViewSpec extends ViewSpec {

  class Setup extends ViewSetup {
    val page: Html = views.html.home()
  }

  "The home page" must {
    "have the correct content type" in new Setup {
      page.contentType mustBe "text/html"
    }

    "display the title correctly" in new Setup {
      document.title mustBe "home.title"
    }

    "display the heading" in new Setup {
      getTextOfElementById("heading") mustBe Some("home.header")
    }

    "contain a form linked to the pages submit action" in new Setup {
      getElementById("home-form").isDefined mustBe true
      getAttrOfElementById("home-form", "action") mustBe Some("/start")
      getAttrOfElementById("home-form", "method") mustBe Some("POST")
    }
  }
}
