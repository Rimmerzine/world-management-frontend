package views.planes

import config.AppConfig
import forms.PlaneForm.{alignmentOptions, planeAlignment, planeDescription, planeName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs.{DropdownWithLabel, InputTextWithLabel, TextAreaWithLabel}

class EditPlaneImpl @Inject()(val appConfig: AppConfig) extends EditPlane

trait EditPlane extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel with HtmlForm {

  private def alignments(implicit messages: Messages): List[(String, String)] = {
    alignmentOptions.map(alignmentValue => (alignmentValue, messages(s"alignment.$alignmentValue")))
  }

  def apply(planeId: String, planeForm: Form[(String, Option[String], String)])(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("edit-plane.title"), twoThirdsWidth)(
      h1(messages("edit-plane.heading")),
      h2(cls := "header-medium")(messages("edit-plane.subheading")),
      form(controllers.planes.routes.EditPlaneController.submit(planeId))(
        div(cls := "form-group")(
          inputTextWithLabel(planeForm(planeName), messages("edit-plane.name.label"))
        ),
        div(cls := "form-group")(
          textAreaWithLabel(planeForm(planeDescription), messages("edit-plane.description.label"))
        ),
        div(cls := "form-group")(
          dropdownWithLabel(planeForm(planeAlignment), alignments, messages("edit-plane.alignment.label"))
        ),
        div(cls := "form-group")(
          button(cls := "btn btn-success", aria.label := messages("edit-plane.save.aria-label"))(messages("common.save"))
        )
      )
    )
  }

}
