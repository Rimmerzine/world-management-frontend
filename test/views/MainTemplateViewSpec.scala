package views

import helpers.ViewSpec
import play.twirl.api.Html

class MainTemplateViewSpec extends ViewSpec {

  class Setup extends ViewSetup {
    val page: Html = views.html.main_template("test.title")(Html("individual page content"))
  }

  "The main template" must {
    "have a title" in new Setup {
      document.title mustBe "test.title"
    }

    "have a navbar at the top" which {
      "exists" in new Setup {
        getElementById("top-navbar").isDefined mustBe true
      }

      "has a brand in the navbar" in new Setup {
        getTextOfElementById("top-navbar-brand") mustBe Some("navbar.top.brand")
      }

      "has a toggle button" in new Setup {
        getElementById("top-navbar-toggle-button").isDefined mustBe true
        getAttrOfElementById("top-navbar-toggle-button", "aria-label") mustBe Some("navbar.top.toggle.aria")
      }

      "has a link to the home page" in new Setup {
        getTextOfElementById("home-link") mustBe Some("navbar.top.links.home")
        getHrefById("home-link") mustBe Some(controllers.routes.HomeController.show().url)
      }

      "has a dropdown link containing tools pages" in new Setup {
        getTextOfElementById("navbar-dropdown-menu-link-top") mustBe Some("navbar.top.tools.dropdown")

      }
    }

    "display the individual pages content" in new Setup {
      getTextOfElementById("page-content") mustBe Some("individual page content")
    }

    "have a navbar at the bottom" which {
      "exists" in new Setup {
        getElementById("bottom-navbar").isDefined mustBe true
      }

      "has a brand in the navbar" in new Setup {
        getTextOfElementById("bottom-navbar-brand") mustBe Some("navbar.bottom.brand")
      }

      "has a toggle button" in new Setup {
        getElementById("bottom-navbar-toggle-button").isDefined mustBe true
        getAttrOfElementById("bottom-navbar-toggle-button", "aria-label") mustBe Some("navbar.bottom.toggle.aria")
      }

      "has a link to the project github" in new Setup {
        getTextOfElementById("github-link") mustBe Some("navbar.bottom.github common.opens-in-new-tab")
        getHrefById("github-link") mustBe Some("https://github.com/Rimmerzine/world-management-frontend")
      }
    }
  }

}
