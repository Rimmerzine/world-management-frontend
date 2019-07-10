package views

import config.AppConfig
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}
import forms.CampaignForm.{campaignDescription, campaignName}

class EditCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends EditCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait EditCampaign extends MainTemplate with InputTextWithLabel with TextAreaWithLabel {

  val messages: Messages
  val appConfig: AppConfig

  def apply(campaignForm: Form[(String, Option[String])], campaignId: String): String = mainTemplate(messages("edit-campaign.title"), "8")(
    h1(messages("edit-campaign.heading")),
    h2(cls := "header-medium")(messages("edit-campaign.subheading")),
    form(action := controllers.routes.EditCampaignController.submit(campaignId).url, method := "POST")(
      div(cls := "form-group")(
        inputTextWithLabel(campaignForm(campaignName), campaignName, campaignName, messages("edit-campaign.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(campaignForm(campaignDescription), campaignDescription, campaignDescription, messages("edit-campaign.description.label"))
      ),
      div(cls := "form-group")(
        button(id := "edit-button", cls := "btn btn-success", aria.label := messages("edit-campaign.edit.aria-label"))(messages("common.save"))
      )
    )
  )

}
