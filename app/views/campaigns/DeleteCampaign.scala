package views.campaigns

import config.AppConfig
import javax.inject.Inject
import models.Campaign
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm

class DeleteCampaignImpl @Inject()(val appConfig: AppConfig) extends DeleteCampaign

trait DeleteCampaign extends MainTemplate with HtmlForm {

  def apply(campaign: Campaign)(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(messages("delete-campaign.title"), twoThirdsWidth)(
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

}
