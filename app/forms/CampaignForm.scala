package forms

import play.api.data.Form
import play.api.data.Forms._

object CampaignForm extends StopOnFirstFail with FormConstraints {

  val campaignName: String = "campaign-name"
  val campaignDescription: String = "campaign-description"

  val campaignNameMaxLength: Int = 50

  val nameMissingError: String = "error.campaign.name.required"
  val nameTooLongError: String = "error.campaign.name.max-length"

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
