package views.lands

import _root_.models.Land
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate

class DeleteLandImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeleteLand {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeleteLand extends MainTemplate {

  val messages: Messages
  val appConfig: AppConfig

  def apply(land: Land): String = mainTemplate(messages("delete-land.title"), "8")(
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
      form(action := controllers.lands.routes.DeleteLandController.submit(land.landId).url, method := "POST")(
        button(cls := "btn btn-success", aria.label := messages("delete-land.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
