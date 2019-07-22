package views.campaigns

import _root_.models.Campaign
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.Card
import views.models.CardLink

class SelectCampaignImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends SelectCampaign {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait SelectCampaign extends MainTemplate with Card {

  val messages: Messages
  val appConfig: AppConfig

  def apply(campaigns: List[Campaign]): String = mainTemplate(messages("select-campaign.title"))(
    h1(cls := "text-center")(messages("select-campaign.heading")),
    div(cls := "form-group")(
      form(action := controllers.campaigns.routes.CreateCampaignController.show().url, method := "GET")(
        button(cls := "btn btn-success btn-block")(messages("select-campaign.create"))
      )

    ),
    div(cls := "row")(
      for (campaign <- campaigns) yield div(cls := "col-lg-4")(card(
        campaign.name,
        campaign.description,
        List(
          CardLink(
            messages("select-campaign.view"),
            controllers.planes.routes.SelectPlaneController.show(campaign.id).url,
            Some(messages("select-campaign.view-aria", campaign.name))
          ),
          CardLink(
            messages("select-campaign.edit"),
            controllers.campaigns.routes.EditCampaignController.show(campaign.id).url,
            Some(messages("select-campaign.edit-aria", campaign.name))
          ),
          CardLink(
            messages("select-campaign.delete"),
            controllers.campaigns.routes.DeleteCampaignController.show(campaign.id).url,
            Some(messages("select-campaign.delete-aria", campaign.name))
          )
        )
      ))
    )
  )

}