package views

import _root_.models._
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.helpers.{Card, CreateElementDropdownLinks}

class SelectElementImpl @Inject()(val appConfig: AppConfig) extends SelectElement

trait SelectElement extends MainTemplate with Card with CreateElementDropdownLinks {

  def apply(campaignId: String, element: WorldElement)(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("select-element.title", messages(s"element.${element.elementType}"), element.name))(
      a(cls := "back-link", href := controllers.routes.SelectController.back().url)(messages("common.back")),
      h1(cls := "text-center")(messages("select-element.heading", messages(s"element.${element.elementType}"), element.name)),
      div(cls := "form-group")(
        createLinks(element.elementType)
      ),
      div(cls := "row")(
        element.content.map { innerElement =>
          div(cls := "col-lg-4")(elementToCard(innerElement))
        }
      )
    )
  }

}
