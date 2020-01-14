package views.campaigns

import config.AppConfig
import javax.inject.Inject
import models.Campaign
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm

class DeleteCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends DeleteCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait DeleteCampaign extends MainTemplate with HtmlForm {

  val messages: Messages

  def apply(campaign: Campaign): TypedTag[String] = mainTemplate(messages("delete-campaign.title"), twoThirdsWidth)(
    h1(messages("delete-campaign.heading")),
    h2(cls := "header-medium")(messages("delete-campaign.subheading")),
    div(cls := "form-group")(
      p(
        span(cls := "font-weight-bold")(messages("delete-campaign.name"))(" "),
        span(campaign.name)
      ),
      campaign.description.map { description =>
        p(
          span(cls := "font-weight-bold")(messages("delete-campaign.description"))(" "),
          span(description)
        )
      }
    ),
    div(cls := "form-group")(
      form(controllers.campaigns.routes.DeleteCampaignController.submit(campaign.id))(
        button(cls := "btn btn-success", aria.label := messages("delete-campaign.confirm.aria-label"))(messages("common.confirm"))
      )
    )
  )

}
