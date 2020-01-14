package views.helpers.inputs

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait DropdownWithLabel {

  val messages: Messages

  def dropdownWithLabel(inputField: Field,
                        options: List[(String, String)],
                        inputLabel: String,
                        ariaLabel: Option[String] = None,
                        includeSelectOption: Boolean = false): Seq[TypedTag[String]] = {

    val isInvalid: String = if (inputField.hasErrors) "is-invalid" else ""

    val dropdownOptions = if (includeSelectOption) {
      List(("select", messages("common.select-an-option"))) ++ options
    } else {
      options
    }

    Seq(
      label(`for` := inputField.id, ariaLabel.map(aria.label := _))(inputLabel),
      select(id := inputField.id, name := inputField.id, cls := s"form-control $isInvalid")(
        for ((optionValue, optionMessage) <- dropdownOptions) yield option(value := optionValue, inputField.value.map {
          case `optionValue` => Some(selected)
          case _ => None
        })(optionMessage)
      )
    ) ++ (for (fieldError <- inputField.errors) yield div(cls := "invalid-feedback")(messages(fieldError.message, fieldError.args)))

  }

}
