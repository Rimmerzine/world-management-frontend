package views

import helpers.ViewSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import play.test.Helpers._
import play.twirl.api.Html

class HomeViewSpec extends ViewSpec with GuiceOneAppPerSuite {

  val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
  val request: FakeRequest[AnyContent] = FakeRequest()
  implicit val messages: Messages = messagesApi.preferred(request)
  val page: Html = views.html.home()
  val pageContent: String = contentAsString(page)

  "The home page" must {
    "have the correct content type" in {
      page.contentType mustBe "text/html"
    }

    "display the title correctly" in  {
      pageContent contains titleHtml("Start creating your D&D world") mustBe true
    }

    "display the header correctly" in {
      pageContent contains headerHtml("Start creating your D&D world") mustBe true
    }

    "contain a form linked to the pages submit action" in {
      pageContent contains formHtml("/start", "POST")
    }
  }
}
