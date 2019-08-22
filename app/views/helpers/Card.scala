package views.helpers

import models.{Campaign, Land, Plane, WorldElement}
import play.api.i18n.Messages
import scalatags.Text
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait Card {

  val messages: Messages

  def elementToCard(campaignId: String, element: WorldElement): TypedTag[String] = {
    element match {
      case campaign: Campaign => campaignCard(campaign)
      case plane: Plane => planeCard(plane)
      case land: Land => landCard(land)
    }
  }

  def card(
            elementId: String, heading: String, optBody: Option[String], viewLink: String, editLink: String, deleteLink: String
          ): Text.TypedTag[String] = {
    div(cls := "card")(
      div(cls := "card-header text-center bg-dark text-light")(heading),
      optBody.map(div(cls := "card-body")(_)),
      div(cls := "card-footer text-center")(
        a(cls := "card-link", href := viewLink, aria.label := messages("card.view-aria", heading))(
          messages("card.view")
        ),
        a(cls := "card-link", href := editLink, aria.label := messages("card.edit-aria", heading))(
          messages("card.edit")
        ),
        a(cls := "card-link", href := deleteLink, aria.label := messages("card.delete-aria", heading))(
          messages("card.delete")
        )
      )
    )
  }

  def campaignCard(campaign: Campaign): Text.TypedTag[String] = {
    card(
      campaign.id,
      campaign.name,
      campaign.description,
      controllers.campaigns.routes.SelectCampaignController.view(campaign.id).url,
      controllers.campaigns.routes.EditCampaignController.show(campaign.id).url,
      controllers.campaigns.routes.DeleteCampaignController.show(campaign.id).url
    )
  }

  def planeCard(plane: Plane): Text.TypedTag[String] = {
    card(
      plane.id,
      s"${plane.name} - ${messages(s"alignment.${plane.alignment}")}",
      plane.description,
      controllers.routes.SelectController.view(plane.id).url,
      controllers.planes.routes.EditPlaneController.show(plane.id).url,
      controllers.planes.routes.DeletePlaneController.show(plane.id).url
    )
  }

  def landCard(land: Land): Text.TypedTag[String] = {
    card(
      land.id,
      land.name,
      land.description,
      controllers.routes.SelectController.view(land.id).url,
      controllers.lands.routes.EditLandController.show(land.id).url,
      controllers.lands.routes.DeleteLandController.show(land.id).url
    )
  }

}
