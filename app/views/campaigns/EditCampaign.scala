package views.campaigns

import config.AppConfig
import forms.CampaignForm.{campaignDescription, campaignName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}

class EditCampaignImpl @Inject()(val appConfig: AppConfig) extends EditCampaign

trait EditCampaign extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with HtmlForm {

  def apply(campaignForm: Form[(String, Option[String])], campaignId: String)(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("edit-campaign.title"), twoThirdsWidth)(
      h1(messages("edit-campaign.heading")),
      h2(cls := "header-medium")(messages("edit-campaign.subheading")),
      form(controllers.campaigns.routes.EditCampaignController.submit(campaignId))(
        div(cls := "form-group")(
          inputTextWithLabel(campaignForm(campaignName), messages("edit-campaign.name.label"))
        ),
        div(cls := "form-group")(
          textAreaWithLabel(campaignForm(campaignDescription), messages("edit-campaign.description.label"))
        ),
        div(cls := "form-group")(
          button(cls := "btn btn-success", aria.label := messages("edit-campaign.save.aria-label"))(messages("common.save"))
        )
      )
    )
  }

}
