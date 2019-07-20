package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait TextAreaWithLabel {

  val messages: Messages

  def textAreaWithLabel(inputField: Field, inputId: String, inputName: String, inputLabel: String, ariaLabel: Option[String] = None): Seq[TypedTag[String]] = {
    val isInvalid: String = if(inputField.hasErrors) "is-invalid" else ""
    Seq(
      label(`for` := inputId, ariaLabel.map(aria.label := _))(inputLabel),
      textarea(id := inputId, name := inputName, cls := s"form-control $isInvalid")(inputField.value)
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message)))
  }

}
