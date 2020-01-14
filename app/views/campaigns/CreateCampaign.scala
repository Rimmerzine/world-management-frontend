package views.campaigns

import config.AppConfig
import forms.CampaignForm.{campaignDescription, campaignName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}

class CreateCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends CreateCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait CreateCampaign extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with HtmlForm {

  val messages: Messages

  def apply(campaignForm: Form[(String, Option[String])]): TypedTag[String] = mainTemplate(messages("create-campaign.title"), twoThirdsWidth)(
    h1(messages("create-campaign.heading")),
    h2(cls := "header-medium")(messages("create-campaign.subheading")),

    form(controllers.campaigns.routes.CreateCampaignController.submit())(
      div(cls := "form-group")(
        inputTextWithLabel(campaignForm(campaignName), messages("create-campaign.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(campaignForm(campaignDescription), messages("create-campaign.description.label"))
      ),
      div(cls := "form-group")(
        button(cls := "btn btn-success", aria.label := messages("create-campaign.create.aria-label"))(messages("common.create"))
      )
    )
  )

}
