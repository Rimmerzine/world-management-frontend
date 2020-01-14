package views.lands

import config.AppConfig
import javax.inject.Inject
import models.Land
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm

class DeleteLandImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeleteLand {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeleteLand extends MainTemplate with HtmlForm {

  val messages: Messages

  def apply(land: Land): TypedTag[String] = mainTemplate(messages("delete-land.title"), twoThirdsWidth)(
    h1(messages("delete-land.heading")),
    h2(cls := "header-medium")(messages("delete-land.subheading")),
    div(cls := "form-group")(
      p(
        span(cls := "font-weight-bold")(messages("delete-land.name"))(" "),
        span(land.name)
      ),
      land.description.map { description =>
        p(
          span(cls := "font-weight-bold")(messages("delete-land.description"))(" "),
          span(description)
        )
      }
    ),
    div(cls := "form-group")(
      form(controllers.lands.routes.DeleteLandController.submit(land.id))(
        button(cls := "btn btn-success", aria.label := messages("delete-land.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
