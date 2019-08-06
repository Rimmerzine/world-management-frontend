package views.lands

import _root_.models.{Land, Plane}
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.Card
import views.models.CardLink

class SelectLandImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends SelectLand {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait SelectLand extends MainTemplate with Card {

  val messages: Messages
  val appConfig: AppConfig

  def apply(plane: Plane, lands: List[Land]): String = mainTemplate(messages("select-land.title", plane.name))(
    h1(cls := "text-center")(messages("select-land.heading", plane.name)),
    h2(cls := "text-center header-medium")(plane.description),
    div(cls := "form-group")(
      form(action := controllers.lands.routes.CreateLandController.show(plane.planeId).url, method := "GET")(
        button(cls := "btn btn-success btn-block")(messages("select-land.create"))
      )
    ),
    div(cls := "row")(
      for (land <- lands) yield div(cls := "col-lg-4")(card(
        land.name,
        land.description,
        List(
          CardLink(
            messages("select-land.view"),
            controllers.lands.routes.SelectLandController.show(plane.planeId).url,
            Some(messages("select-land.view-aria", land.name))
          ),
          CardLink(
            messages("select-land.edit"),
            controllers.lands.routes.EditLandController.show(land.landId).url,
            Some(messages("select-land.edit-aria", land.name))
          ),
          CardLink(
            messages("select-land.delete"),
            controllers.lands.routes.DeleteLandController.show(land.landId).url,
            Some(messages("select-land.delete-aria", land.name))
          )
        )
      ))
    )
  )

}
