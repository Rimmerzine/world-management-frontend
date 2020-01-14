package views.creatures

import config.AppConfig
import forms.SelectCreatureForm._
import javax.inject.Inject
import models.Creature
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.inputs.DropdownWithInlineLabel
import views.helpers.{Card, HtmlForm}

class SelectCreatureImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends SelectCreature {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait SelectCreature extends MainTemplate with Card with DropdownWithInlineLabel with HtmlForm {

  implicit val messages: Messages

  def apply(
             selectCreatureForm: Form[(String, String)],
             creatures: List[Creature],
             challengeRatings: List[String],
             nameStarts: List[String]
           ): TypedTag[String] = {

    mainTemplate(messages("select-creature.title"))(
      h1(cls := "text-center")(messages("select-creature.heading")),
      div(cls := "form-group")(
        a(cls := "btn btn-success btn-block", href := controllers.creatures.routes.CreateCreatureController.show().url)(messages("select-creature.create"))
      ),
      div(cls := "form-group")(
        div(cls := "bold")(messages("select-creature.filters.heading")),
        form(controllers.creatures.routes.SelectCreatureController.filter())(
          div(cls := "form-inline")(
            div(cls := "form-group form-group-inline")(
              dropdownWithInlineLabel(
                selectCreatureForm(selectCreatureAlphabetically),
                createNameStartDropdownOptions(nameStarts),
                messages("select-creature.filters.name-start"),
                Some(messages("select-creature.filters.name-start-aria"))
              )
            ),
            div(cls := "form-group form-group-inline")(
              dropdownWithInlineLabel(
                selectCreatureForm(selectCreatureChallenge),
                createChallengeRatingsOptions(challengeRatings),
                messages("select-creature.filters.challenge-rating"),
                Some(messages("select-creature.filters.challenge-rating-aria"))
              )
            ),
            div(cls := "form-group form-group-inline")(
              button(cls := "btn btn-success", aria.label := messages("select-creature.filters.button-aria"))(messages("select-creature.filters.button"))
            )
          )
        )
      ),
      div(cls := "row")(
        for (creature <- creatures) yield div(cls := "col-lg-4")(
          creatureCard(creature)
        )
      )
    )
  }

}
