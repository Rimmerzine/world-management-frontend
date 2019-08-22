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

class CreatePlaneImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends CreatePlane {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait CreatePlane extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel {

  val messages: Messages
  val appConfig: AppConfig

  val alignments: List[(String, String)] = alignmentOptions.map(alignmentValue => (alignmentValue, messages(s"alignment.$alignmentValue")))

  def apply(planeForm: Form[(String, Option[String], String)]): TypedTag[String] = mainTemplate(messages("create-plane.title"), "8")(
    h1(messages("create-plane.heading")),
    h2(cls := "header-medium")(messages("create-plane.subheading")),
    form(action := controllers.planes.routes.CreatePlaneController.submit().url, method := "POST")(
      div(cls := "form-group")(
        inputTextWithLabel(planeForm(planeName), planeName, planeName, messages("create-plane.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(planeForm(planeDescription), planeDescription, planeDescription, messages("create-plane.description.label"))
      ),
      div(cls := "form-group")(
        dropdownWithLabel(planeForm(planeAlignment), planeAlignment, alignments, messages("create-plane.alignment.label"))
      ),
      div(cls := "form-group")(
        button(cls := "btn btn-success", aria.label := messages("create-plane.create.aria-label"))(messages("common.create"))
      )
    )
  )

}
