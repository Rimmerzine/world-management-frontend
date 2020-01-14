package views.planes

import config.AppConfig
import javax.inject.Inject
import models.Plane
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm

class DeletePlaneImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeletePlane {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeletePlane extends MainTemplate with HtmlForm {

  val messages: Messages

  def apply(plane: Plane): TypedTag[String] = mainTemplate(messages("delete-plane.title"), twoThirdsWidth)(
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
      form(controllers.planes.routes.DeletePlaneController.submit(plane.id))(
        button(cls := "btn btn-success", aria.label := messages("delete-plane.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
