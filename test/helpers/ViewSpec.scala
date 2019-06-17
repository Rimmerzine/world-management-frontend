package helpers

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import play.api.i18n.{DefaultMessagesApi, Messages}
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import play.twirl.api.Html
import play.test.Helpers.contentAsString

trait ViewSpec extends UnitSpec {

  val messagesApi = new DefaultMessagesApi(Map.empty[String, Map[String, String]])
  implicit val request: FakeRequest[AnyContent] = FakeRequest()
  implicit val messages: Messages = messagesApi.preferred(request)

  trait ViewSetup {

    val page: Html
    lazy val document: Document = Jsoup.parse(contentAsString(page))

    def getElementById(id: String): Option[Element] = Option(document.getElementById(id))
    def getTextOfElementById(id: String): Option[String] = getElementById(id).map(_.text)
    def getAttrOfElementById(id: String, attribute: String): Option[String] = getElementById(id).map(_.attr(attribute))
    def getHrefById(id: String): Option[String] = getElementById(id).map(_.attr("href"))

  }

}