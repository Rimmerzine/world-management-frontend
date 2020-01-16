package views.errors

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate

class NotFoundImpl @Inject()(val appConfig: AppConfig) extends NotFound

trait NotFound extends MainTemplate {

  def apply(implicit messages: Messages): TypedTag[String] = mainTemplate(messages("error.not-found.title"), twoThirdsWidth)(
    h1(cls := "text-center")(messages("error.not-found.heading"))
  )

}
