package views.errors

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate

class InternalServerErrorImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends InternalServerError {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait InternalServerError extends MainTemplate {

  val messages: Messages

  def apply(): TypedTag[String] = mainTemplate(messages("error.internal-server-error.title"), twoThirdsWidth)(
    h1(cls := "text-center")(messages("error.internal-server-error.heading")),
    h2(cls := "text-center")(messages("error.internal-server-error.subheading"))
  )

}
