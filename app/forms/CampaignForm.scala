package forms

import play.api.data.Form
import play.api.data.Forms._

object CampaignForm extends StopOnFirstFail with FormConstraints {

  val campaignName: String = "campaign-name"
  val campaignDescription: String = "campaign-description"

  val campaignNameMaxLength: Int = 50

  val nameMissingError: ErrorWithArgs = ErrorWithArgs("error.campaign.name.required")
  val nameTooLongError: ErrorWithArgs = ErrorWithArgs("error.campaign.name.max-length")

  val form: Form[(String, Option[String])] = Form(
    tuple(
      campaignName -> default(text, "").verifyingFirst(
        nonEmpty(nameMissingError),
        maxLength(campaignNameMaxLength, nameTooLongError)
      ),
      campaignDescription -> optional(text)
    )
  )

}
