package forms

import play.api.data.Form
import play.api.data.Forms._

object PlaneForm extends StopOnFirstFail with FormConstraints {

  val alignmentOptions: List[String] = List(
    "lawful-good",
    "lawful-neutral",
    "lawful-evil",
    "neutral-good",
    "neutral",
    "neutral-evil",
    "chaotic-good",
    "chaotic-neutral",
    "chaotic-evil",
    "unaligned"
  )

  val planeName: String = "plane-name"
  val planeDescription: String = "plane-description"
  val planeAlignment: String = "plane-alignment"

  val planeNameMaxLength: Int = 50

  val nameMissingError: String = "error.plane.name.required"
  val nameTooLongError: String = "error.plane.name.max-length"

  val alignmentRequiredError: String = "error.plane.alignment.required"

  val form: Form[(String, Option[String], String)] = Form(
    tuple(
      planeName -> default(text, "").verifying(stopOnFirstFail(
        nonEmptyConstraint(nameMissingError),
        maxLengthConstraint(planeNameMaxLength, nameTooLongError)
      )),
      planeDescription -> optional(text),
      planeAlignment -> default(text, "").verifying(stopOnFirstFail(
        nonEmptyConstraint(alignmentRequiredError),
        validOptionConstraint(alignmentOptions, alignmentRequiredError)
      ))
    )
  )

}
