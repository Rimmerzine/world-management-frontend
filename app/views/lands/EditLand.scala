package views.lands

import config.AppConfig
import forms.LandForm.{landDescription, landName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs.{DropdownWithLabel, InputTextWithLabel, TextAreaWithLabel}

class EditLandImpl @Inject()(val appConfig: AppConfig) extends EditLand

trait EditLand extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel with HtmlForm {

  def apply(landId: String, landForm: Form[(String, Option[String])])(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("edit-land.title"), twoThirdsWidth)(
      h1(messages("edit-land.heading")),
      h2(cls := "header-medium")(messages("edit-land.subheading")),
      form(controllers.lands.routes.EditLandController.submit(landId))(
        div(cls := "form-group")(
          inputTextWithLabel(landForm(landName), messages("edit-land.name.label"))
        ),
        div(cls := "form-group")(
          textAreaWithLabel(landForm(landDescription), messages("edit-land.description.label"))
        ),
        div(cls := "form-group")(
          button(cls := "btn btn-success", aria.label := messages("edit-land.save.aria-label"))(messages("common.save"))
        )
      )
    )
  }

}
