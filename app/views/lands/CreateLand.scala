package views.lands

import config.AppConfig
import forms.LandForm.{landDescription, landName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs.{InputTextWithLabel, TextAreaWithLabel}

class CreateLandImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends CreateLand {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait CreateLand extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with HtmlForm {

  val messages: Messages

  def apply(landForm: Form[(String, Option[String])]): TypedTag[String] = mainTemplate(messages("create-land.title"), twoThirdsWidth)(
    h1(messages("create-land.heading")),
    h2(cls := "header-medium")(messages("create-land.subheading")),
    form(controllers.lands.routes.CreateLandController.submit())(
      div(cls := "form-group")(
        inputTextWithLabel(landForm(landName), messages("create-land.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(landForm(landDescription), messages("create-land.description.label"))
      ),
      div(cls := "form-group")(
        button(cls := "btn btn-success", aria.label := messages("create-land.create.aria-label"))(messages("common.create"))
      )
    )
  )

}
