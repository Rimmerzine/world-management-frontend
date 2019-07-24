package views.planes

import _root_.models.{Campaign, Plane}
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.{Langs, Messages, MessagesApi, MessagesImpl}
import scalatags.Text.all._
import views.MainTemplate
import views.helpers.Card
import views.models.CardLink

class SelectPlaneImpl @Inject()(messagesApi: MessagesApi, langs: Langs, val appConfig: AppConfig) extends SelectPlane {

  lazy val messages: Messages = MessagesImpl(langs.availables.head, messagesApi)

}

trait SelectPlane extends MainTemplate with Card {

  val messages: Messages
  val appConfig: AppConfig

  def apply(campaign: Campaign, planes: List[Plane]): String = mainTemplate(messages("select-plane.title", campaign.name))(
    h1(cls := "text-center")(messages("select-plane.heading", campaign.name)),
    h2(cls := "text-center header-medium")(campaign.description),
    div(cls := "form-group")(
      form(action := controllers.planes.routes.CreatePlaneController.show(campaign.id).url, method := "GET")(
        button(cls := "btn btn-success btn-block")(messages("select-plane.create"))
      )
    ),
    div(cls := "row")(
      for (plane <- planes) yield div(cls := "col-lg-4")(card(
        s"${plane.name} - ${messages(s"alignment.${plane.alignment}")}",
        plane.description,
        List(
          CardLink(
            messages("select-plane.view"),
            controllers.planes.routes.SelectPlaneController.show(campaign.id).url,
            Some(messages("select-plane.view-aria", plane.name))
          ),
          CardLink(
            messages("select-plane.edit"),
            controllers.planes.routes.EditPlaneController.show(plane.planeId).url,
            Some(messages("select-plane.edit-aria", plane.name))
          ),
          CardLink(
            messages("select-plane.delete"),
            controllers.planes.routes.DeletePlaneController.show(plane.planeId).url,
            Some(messages("select-plane.delete-aria", plane.name))
          )
        )
      ))
    )
  )

}
