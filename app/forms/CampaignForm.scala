package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid}


object CampaignForm extends StopOnFirstFail {

  val campaignName: String = "campaign-name"
  val campaignDescription: String = "campaign-description"

  val campaignNameMaxLength: Int = 50

  val nameMissingError: String = "error.campaign.name.required"
  val nameTooLongError: String = "error.campaign.name.max-length"

  def nonEmptyConstraint(error: String): Constraint[String] = Constraint { text: String =>
    if (text.nonEmpty) Valid else Invalid(error)
  }

  def maxLengthConstraint(maxLength: Int, error: String): Constraint[String] = Constraint { text: String =>
    if (text.length <= maxLength) Valid else Invalid(error)
  }

  val form: Form[(String, Option[String])] = Form(
    tuple(
      campaignName -> default(text, "").verifying(stopOnFirstFail(
        nonEmptyConstraint(nameMissingError),
        maxLengthConstraint(campaignNameMaxLength, nameTooLongError)
      )),
      campaignDescription -> optional(text)
    )
  )

}
