package views.planes

import _root_.models.Plane
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate

class DeletePlaneImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeletePlane {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeletePlane extends MainTemplate {

  val messages: Messages
  val appConfig: AppConfig

  def apply(plane: Plane): String = mainTemplate(messages("delete-plane.title"), "8")(
    h1(messages("delete-plane.heading")),
    h2(cls := "header-medium")(messages("delete-plane.subheading")),
    div(cls := "form-group")(
      p(
        span(cls := "font-weight-bold")(messages("delete-plane.name"))(" "),
        span(plane.name)
      ),
      plane.description.map { description =>
        p(
          span(cls := "font-weight-bold")(messages("delete-plane.description"))(" "),
          span(description)
        )
      },
      p(
        span(cls := "font-weight-bold")(messages("delete-plane.alignment"))(" "),
        span(messages(s"alignment.${plane.alignment}"))
      )
    ),
    div(cls := "form-group")(
      form(action := controllers.planes.routes.DeletePlaneController.submit(plane.planeId).url, method := "POST")(
        button(cls := "btn btn-success", aria.label := messages("delete-plane.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
