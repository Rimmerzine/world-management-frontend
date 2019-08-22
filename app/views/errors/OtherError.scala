package views.errors

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate

class OtherErrorImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends OtherError {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait OtherError extends MainTemplate {

  val messages: Messages
  val appConfig: AppConfig

  def apply(): TypedTag[String] = mainTemplate(messages("error.other-error.title"), "8")(
    h1(cls := "text-center")(messages("error.other-error.heading")),
    h2(cls := "text-center")(messages("error.internal-server-error.subheading"))
  )

}
