package views.helpers

import models.{Campaign, Creature, Land, Plane, WorldElement}
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

  def cardLink(linkHref: String, linkText: String, ariaLabel: String): TypedTag[String] = {
    a(cls := "card-link", href := linkHref, aria.label := ariaLabel)(linkText)
  }

  private val viewLink: (String, String) => TypedTag[String] = cardLink(_, messages("card.view"), _)
  private val editLink: (String, String) => TypedTag[String] = cardLink(_, messages("card.edit"), _)
  private val deleteLink: (String, String) => TypedTag[String] = cardLink(_, messages("card.delete"), _)

  def card(heading: String, body: Option[Text.Modifier], links: List[TypedTag[String]]): Text.TypedTag[String] = {
    div(cls := "card")(
      div(cls := "card-header text-center bg-dark text-light")(heading),
      body.map(bodyContent =>
        div(cls := "card-body")(
          bodyContent
        )
      ),
      div(cls := "card-footer text-center")(links)
    )
  }

  def campaignCard(campaign: Campaign): TypedTag[String] = {
    card(
      campaign.name,
      campaign.description.map(div(cls := "card-text")(_)),
      List(
        viewLink(controllers.campaigns.routes.SelectCampaignController.view(campaign.id).url, messages("card.view-aria", campaign.name)),
        editLink(controllers.campaigns.routes.EditCampaignController.show(campaign.id).url, messages("card.edit-aria", campaign.name)),
        deleteLink(controllers.campaigns.routes.DeleteCampaignController.show(campaign.id).url, messages("card.delete-aria", campaign.name))
      )
    )
  }

  def planeCard(plane: Plane): TypedTag[String] = {
    card(
      s"${plane.name} - ${messages(s"alignment.${plane.alignment}")}",
      plane.description.map(div(cls := "card-text")(_)),
      List(
        viewLink(controllers.routes.SelectController.view(plane.id).url, messages("card.view-aria", plane.name)),
        editLink(controllers.planes.routes.EditPlaneController.show(plane.id).url, messages("card.view-aria", plane.name)),
        deleteLink(controllers.planes.routes.DeletePlaneController.show(plane.id).url, messages("card.delete-aria", plane.name))
      )
    )
  }

  def landCard(land: Land): TypedTag[String] = {
    card(
      land.name,
      land.description.map(div(cls := "card-text")(_)),
      List(
        viewLink(controllers.routes.SelectController.view(land.id).url, messages("card.view-aria", land.name)),
        editLink(controllers.lands.routes.EditLandController.show(land.id).url, messages("card.view-aria", land.name)),
        deleteLink(controllers.lands.routes.DeleteLandController.show(land.id).url, messages("card.delete-aria", land.name))
      )
    )
  }

  def creatureCard(creature: Creature): TypedTag[String] = {

    card(
      creature.detail.name,
      Some(div(cls := "card-text")(
        creature.detail.description
      )),
      List()
    )
  }

}
