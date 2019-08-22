package views.planes

import config.AppConfig
import forms.PlaneForm.{alignmentOptions, planeAlignment, planeDescription, planeName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.inputs.{DropdownWithLabel, InputTextWithLabel, TextAreaWithLabel}

class EditPlaneImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends EditPlane {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait EditPlane extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel {

  val messages: Messages
  val appConfig: AppConfig

  val alignments: List[(String, String)] = alignmentOptions.map(alignmentValue => (alignmentValue, messages(s"alignment.$alignmentValue")))

  def apply(planeId: String, planeForm: Form[(String, Option[String], String)]): TypedTag[String] = mainTemplate(messages("edit-plane.title"), "8")(
    h1(messages("edit-plane.heading")),
    h2(cls := "header-medium")(messages("edit-plane.subheading")),
    form(action := controllers.planes.routes.EditPlaneController.submit(planeId).url, method := "POST")(
      div(cls := "form-group")(
        inputTextWithLabel(planeForm(planeName), planeName, planeName, messages("edit-plane.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(planeForm(planeDescription), planeDescription, planeDescription, messages("edit-plane.description.label"))
      ),
      div(cls := "form-group")(
        dropdownWithLabel(planeForm(planeAlignment), planeAlignment, alignments, messages("edit-plane.alignment.label"))
      ),
      div(cls := "form-group")(
        button(cls := "btn btn-success", aria.label := messages("edit-plane.save.aria-label"))(messages("common.save"))
      )
    )
  )

}
