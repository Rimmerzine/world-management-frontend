package views

import config.AppConfig
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}
import forms.CampaignForm.{campaignDescription, campaignName}

class CreateCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends CreateCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait CreateCampaign extends MainTemplate with InputTextWithLabel with TextAreaWithLabel {

  val messages: Messages
  val appConfig: AppConfig

  def apply(campaignForm: Form[(String, Option[String])]): String = mainTemplate(messages("create-campaign.title"), "8")(
    h1(messages("create-campaign.heading")),
    h2(cls := "header-medium")(messages("create-campaign.subheading")),
    form(action := controllers.routes.CreateCampaignController.submit().url, method := "POST")(
      div(cls := "form-group")(
        inputTextWithLabel(campaignForm(campaignName), campaignName, campaignName, messages("create-campaign.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(campaignForm(campaignDescription), campaignDescription, campaignDescription, messages("create-campaign.description.label"))
      ),
      div(cls := "form-group")(
        button(id := "create-button", cls := "btn btn-success", aria.label := messages("create-campaign.create.aria-label"))(messages("common.save"))
      )
    )
  )

}
