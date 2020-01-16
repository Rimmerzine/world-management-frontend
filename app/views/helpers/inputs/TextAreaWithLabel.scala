package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait TextAreaWithLabel {

  def textAreaWithLabel(inputField: Field, inputLabel: String, ariaLabel: Option[String] = None)(implicit messages: Messages): Seq[TypedTag[String]] = {
    val isInvalid: String = if (inputField.hasErrors) "is-invalid" else ""
    Seq(
      label(`for` := inputField.id, ariaLabel.map(aria.label := _))(inputLabel),
      textarea(id := inputField.id, name := inputField.name, cls := s"form-control $isInvalid")(inputField.value)
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message, fieldError.args)))
  }

}
