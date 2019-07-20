package views.helpers

import scalatags.Text
import scalatags.Text.all._
import views.models.CardLink

trait Card {

  def card(header: String, optBody: Option[String], links: List[CardLink]): Text.TypedTag[String] = div(cls := "card")(
    div(cls := "card-header text-center bg-dark text-light")(header),
    optBody.map(div(cls := "card-body")(_)),
    links.headOption.map { _ =>
      div(cls := "card-footer text-center")(
        for (cardLink <- links) yield a(cls := "card-link", href := cardLink.href, cardLink.ariaLabel.map(aria.label := _))(cardLink.text)
      )
    }
  )

}
