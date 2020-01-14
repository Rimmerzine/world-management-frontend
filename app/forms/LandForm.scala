package forms

import play.api.data.Form
import play.api.data.Forms._

object LandForm extends StopOnFirstFail with FormConstraints {

  val landName: String = "land-name"
  val landDescription: String = "land-description"

  val landMaxLength: Int = 50

  val nameMissingError: ErrorWithArgs = ErrorWithArgs("error.land.name.required")
  val nameTooLongError: ErrorWithArgs = ErrorWithArgs("error.land.name.max-length")

  val form: Form[(String, Option[String])] = Form(
    tuple(
      landName -> default(text, "").verifyingFirst(
        nonEmpty(nameMissingError),
        maxLength(landMaxLength, nameTooLongError)
      ),
      landDescription -> optional(text)
    )
  )

}
