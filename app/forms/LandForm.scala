package forms

import play.api.data.Form
import play.api.data.Forms._

object LandForm extends StopOnFirstFail with FormConstraints {

  val landName: String = "land-name"
  val landDescription: String = "land-description"

  val landMaxLength: Int = 50

  val nameMissingError: String = "error.land.name.required"
  val nameTooLongError: String = "error.land.name.max-length"

  val alignmentRequiredError: String = "error.land.alignment.required"

  val form: Form[(String, Option[String])] = Form(
    tuple(
      landName -> default(text, "").verifying(stopOnFirstFail(
        nonEmptyConstraint(nameMissingError),
        maxLengthConstraint(landMaxLength, nameTooLongError)
      )),
      landDescription -> optional(text)
    )
  )

}
