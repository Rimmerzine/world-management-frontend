package views

import _root_.models.Campaign
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}

class DeleteCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeleteCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeleteCampaign extends MainTemplate {

  val messages: Messages
  val appConfig: AppConfig

  def apply(campaign: Campaign): String = mainTemplate(messages("delete-campaign.title"), "8")(
    h1(messages("delete-campaign.heading")),
    h2(cls := "header-medium")(messages("delete-campaign.subheading")),
    div(cls := "form-group")(
      p(
        span(cls := "font-weight-bold")(messages("delete-campaign.name")),
        span(campaign.name)
      ),
      campaign.description.map { description =>
        p(
          span(cls := "font-weight-bold")(messages("delete-campaign.description")),
          span(description)
        )
      }
    ),
    div(cls := "form-group")(
      form(action := controllers.routes.DeleteCampaignController.submit(campaign.id).url, method := "POST")(
        button(cls := "btn btn-success", aria.label := messages("delete-campaign.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
