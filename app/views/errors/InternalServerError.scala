package views.errors

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate

class InternalServerErrorImpl @Inject()(val appConfig: AppConfig) extends InternalServerError

trait InternalServerError extends MainTemplate {

  def apply(implicit messages: Messages): TypedTag[String] = mainTemplate(messages("error.internal-server-error.title"), twoThirdsWidth)(
    h1(cls := "text-center")(messages("error.internal-server-error.heading")),
    h2(cls := "text-center")(messages("error.internal-server-error.subheading"))
  )

}
