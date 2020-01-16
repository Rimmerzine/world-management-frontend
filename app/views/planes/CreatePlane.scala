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

class CreatePlaneImpl @Inject()(val appConfig: AppConfig) extends CreatePlane

trait CreatePlane extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel with HtmlForm {

  private def alignments(implicit messages: Messages): List[(String, String)] = {
    alignmentOptions.map(alignmentValue => (alignmentValue, messages(s"alignment.$alignmentValue")))
  }

  def apply(planeForm: Form[(String, Option[String], String)])(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("create-plane.title"), twoThirdsWidth)(
      h1(messages("create-plane.heading")),
      h2(cls := "header-medium")(messages("create-plane.subheading")),
      form(controllers.planes.routes.CreatePlaneController.submit())(
        div(cls := "form-group")(
          inputTextWithLabel(planeForm(planeName), messages("create-plane.name.label"))
        ),
        div(cls := "form-group")(
          textAreaWithLabel(planeForm(planeDescription), messages("create-plane.description.label"))
        ),
        div(cls := "form-group")(
          dropdownWithLabel(planeForm(planeAlignment), alignments, messages("create-plane.alignment.label"))
        ),
        div(cls := "form-group")(
          button(cls := "btn btn-success", aria.label := messages("create-plane.create.aria-label"))(messages("common.create"))
        )
      )
    )
  }

}
