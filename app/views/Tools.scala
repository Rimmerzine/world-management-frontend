package views

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.helpers.Card

class ToolsImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends Tools {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait Tools extends MainTemplate with Card {

  val messages: Messages
  val appConfig: AppConfig

  def apply(): TypedTag[String] = mainTemplate(messages("tools.title"))(
    h1(cls := "text-center")(messages("tools.heading")),
    div(cls := "row")(
      div(cls := "col-xl-6")(
        card(
          heading = messages("tools.creatures-card.heading"),
          body = Some(div(cls := "card-text")(
            p(messages("tools.creatures-card.line-1")),
            p(messages("tools.creatures-card.line-2")),
            ul(
              li(messages("tools.creatures-card.create")),
              li(messages("tools.creatures-card.view")),
              li(messages("tools.creatures-card.edit")),
              li(messages("tools.creatures-card.delete")),
              li(messages("tools.creatures-card.clone"))
            )
          )),
          links = List(
            cardLink(
              controllers.creatures.routes.SelectCreatureController.show().url,
              messages("tools.creatures-card.continue"),
              messages("tools.creatures-card.continue-aria")
            )
          )
        )
      )
    )
  )

}
