package views.helpers

import models._
import play.api.i18n.Messages
import scalatags.Text
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait Card {

  private def toCardText(text: String): TypedTag[String] = div(cls := "card-text")(text)

  def elementToCard(element: WorldElement)(implicit messages: Messages): TypedTag[String] = {
    element match {
      case campaign: Campaign => campaignCard(campaign)
      case plane: Plane => planeCard(plane)
      case land: Land => landCard(land)
    }
  }

  def cardLink(linkHref: String, linkText: String, ariaLabel: String): TypedTag[String] = {
    a(cls := "card-link", href := linkHref, aria.label := ariaLabel)(linkText)
  }

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

  def campaignCard(campaign: Campaign)(implicit messages: Messages): TypedTag[String] = {
    card(
      heading = campaign.name,
      body = campaign.description.map(toCardText),
      links = List(
        cardLink(
          linkHref = controllers.campaigns.routes.SelectCampaignController.view(campaign.id).url,
          linkText = messages("card.view"),
          ariaLabel = messages("card.view-aria", campaign.name)
        ),
        cardLink(
          linkHref = controllers.campaigns.routes.EditCampaignController.show(campaign.id).url,
          linkText = messages("card.edit"),
          ariaLabel = messages("card.edit-aria", campaign.name)
        ),
        cardLink(
          linkHref = controllers.campaigns.routes.DeleteCampaignController.show(campaign.id).url,
          linkText = messages("card.delete"),
          ariaLabel = messages("card.delete-aria", campaign.name)
        )
      )
    )
  }

  def planeCard(plane: Plane)(implicit messages: Messages): TypedTag[String] = {
    card(
      heading = s"${plane.name} - ${messages(s"alignment.${plane.alignment}")}",
      body = plane.description.map(toCardText),
      links = List(
        cardLink(
          linkHref = controllers.routes.SelectController.view(plane.id).url,
          linkText = messages("card.view"),
          ariaLabel = messages("card.view-aria", plane.name)
        ),
        cardLink(
          linkHref = controllers.planes.routes.EditPlaneController.show(plane.id).url,
          linkText = messages("card.edit"),
          ariaLabel = messages("card.view-aria", plane.name)
        ),
        cardLink(
          linkHref = controllers.planes.routes.DeletePlaneController.show(plane.id).url,
          linkText = messages("card.delete"),
          ariaLabel = messages("card.delete-aria", plane.name)
        )
      )
    )
  }

  def landCard(land: Land)(implicit messages: Messages): TypedTag[String] = {
    card(
      heading = land.name,
      body = land.description.map(toCardText),
      links = List(
        cardLink(
          linkHref = controllers.routes.SelectController.view(land.id).url,
          linkText = messages("card.view"),
          ariaLabel = messages("card.view-aria", land.name)
        ),
        cardLink(
          linkHref = controllers.lands.routes.EditLandController.show(land.id).url,
          linkText = messages("card.edit"),
          ariaLabel = messages("card.view-aria", land.name)
        ),
        cardLink(
          linkHref = controllers.lands.routes.DeleteLandController.show(land.id).url,
          linkText = messages("card.delete"),
          ariaLabel = messages("card.delete-aria", land.name)
        )
      )
    )
  }

  def creatureCard(creature: Creature): TypedTag[String] = {
    card(
      heading = creature.detail.name,
      body = creature.detail.description.map(toCardText),
      List()
    )
  }

}
