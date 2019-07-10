package views.errors

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate

class NotFoundImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends NotFound {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait NotFound extends MainTemplate {

  val messages: Messages
  val appConfig: AppConfig

  def apply(): String = mainTemplate(messages("error.not-found.title"), "8")(
    h1(cls := "text-center")(messages("error.not-found.heading"))
  )

}
