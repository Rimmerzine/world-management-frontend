package views.campaigns

import config.AppConfig
import javax.inject.Inject
import models.Campaign
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.Card

class SelectCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends SelectCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait SelectCampaign extends MainTemplate with Card {

  val messages: Messages

  def apply(campaigns: List[Campaign]): TypedTag[String] = mainTemplate(messages("select-campaign.title"))(
    h1(cls := "text-center")(messages("select-campaign.heading")),
    div(cls := "form-group")(
      a(cls := "btn btn-success btn-block", href := controllers.campaigns.routes.CreateCampaignController.show().url)(messages("select-campaign.create"))
    ),
    div(cls := "row")(
      for (campaign <- campaigns) yield div(cls := "col-lg-4")(
        campaignCard(campaign)
      )
    )
  )

}
