package views

import _root_.models.Campaign
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
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
    h2(cls := "text-center")(messages("select-campaign.subheading")),
    div(cls := "form-group")(
      form(action := controllers.routes.CreateCampaignController.show().url, method := "GET")(
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
            s"/???",
            Some(messages("select-campaign.view-aria", campaign.name))
          ),
          CardLink(
            messages("select-campaign.edit"),
            controllers.routes.EditCampaignController.show(campaign.id).url,
            Some(messages("select-campaign.edit-aria", campaign.name))
          ),
          CardLink(
            messages("select-campaign.delete"),
            controllers.routes.DeleteCampaignController.show(campaign.id).url,
            Some(messages("select-campaign.delete-aria", campaign.name))
          )
        )
      ))
    )
  )

}
