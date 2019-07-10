package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait InputTextWithLabel {

  val messages: Messages

  def inputTextWithLabel(inputField: Field, inputId: String, inputName: String, inputLabel: String, ariaLabel: Option[String] = None): Seq[TypedTag[String]] = {
    val isInvalid: String = if(inputField.hasErrors) "is-invalid" else ""
    Seq(
      label(`for` := inputId, ariaLabel.map(aria.label := _))(inputLabel),
      input(`type` := "text", id := inputId, name := inputName, cls := s"form-control $isInvalid", inputField.value.map(value := _))
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message)))
  }

}
