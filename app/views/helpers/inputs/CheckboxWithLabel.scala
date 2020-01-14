package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait CheckboxWithLabel {

  val messages: Messages

  def checkboxWithLabel(inputField: Field, inputLabel: String,  ariaLabel: Option[String] = None): Seq[TypedTag[String]] = {
    val isInvalid: String = if (inputField.hasErrors) "is-invalid" else ""
    val checkedValue = inputField.value match {
      case Some("true") => Some(checked := "checked")
      case _ => None
    }
    Seq(
      input(`type` := "checkbox", id := inputField.id, name := inputField.name, cls := s"form-check-input $isInvalid", value := "true", checkedValue),
      label(`for` := inputField.id, cls := "form-check-label", ariaLabel.map(aria.label := _))(inputLabel)
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message, fieldError.args)))
  }

}
