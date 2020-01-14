package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait RadioButtonNoLabel {

  val messages: Messages

  def radioButtonNoLabel(inputField: Field, radioValue: String, ariaLabel: Option[String] = None): Seq[TypedTag[String]] = {
    val isInvalid: String = if (inputField.hasErrors) "is-invalid" else ""
    val checkedValue = inputField.value match {
      case Some(`radioValue`) => Some(checked := "checked")
      case _ => None
    }
    Seq(
      input(
        `type` := "radio",
        id := inputField.id,
        name := inputField.name,
        cls := s"form-check-input $isInvalid",
        value := radioValue,
        checkedValue,
        ariaLabel.map(aria.label := _),
      )
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message, fieldError.args)))
  }

}
