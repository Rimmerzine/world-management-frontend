package views.lands

import config.AppConfig
import forms.LandForm.{landDescription, landName}
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.inputs.{DropdownWithLabel, InputTextWithLabel, TextAreaWithLabel}

class EditLandImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends EditLand {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait EditLand extends MainTemplate with InputTextWithLabel with TextAreaWithLabel with DropdownWithLabel {

  val messages: Messages
  val appConfig: AppConfig

  def apply(landId: String, landForm: Form[(String, Option[String])]): TypedTag[String] = mainTemplate(messages("edit-land.title"), "8")(
    h1(messages("edit-land.heading")),
    h2(cls := "header-medium")(messages("edit-land.subheading")),
    form(action := controllers.lands.routes.EditLandController.submit(landId).url, method := "POST")(
      div(cls := "form-group")(
        inputTextWithLabel(landForm(landName), landName, landName, messages("edit-land.name.label"))
      ),
      div(cls := "form-group")(
        textAreaWithLabel(landForm(landDescription), landDescription, landDescription, messages("edit-land.description.label"))
      ),
      div(cls := "form-group")(
        button(cls := "btn btn-success", aria.label := messages("edit-land.save.aria-label"))(messages("common.save"))
      )
    )
  )

}