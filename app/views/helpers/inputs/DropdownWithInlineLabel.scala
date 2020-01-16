package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait DropdownWithInlineLabel {

  def dropdownWithInlineLabel(inputField: Field,
                              options: List[(String, String)],
                              inputLabel: String,
                              ariaLabel: Option[String] = None)
                             (implicit messages: Messages): Seq[TypedTag[String]] = {

    val isInvalid: String = if (inputField.hasErrors) "is-invalid" else ""

    Seq(
      label(cls := "inline-label", `for` := inputField.id, ariaLabel.map(aria.label := _))(inputLabel),
      select(id := inputField.id, name := inputField.id, cls := s"form-control $isInvalid")(
        for ((optionValue, optionMessage) <- options) yield option(value := optionValue, inputField.value.map {
          case `optionValue` => Some(selected)
          case _ => None
        })(optionMessage)
      )
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message, fieldError.args)))

  }

}
