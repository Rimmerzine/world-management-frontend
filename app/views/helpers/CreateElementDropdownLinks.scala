package views.helpers

import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

trait CreateElementDropdownLinks {

  private val elementCreateRoutes: List[(String, String)] = List(
    "campaign" -> controllers.campaigns.routes.CreateCampaignController.show().url,
    "plane" -> controllers.planes.routes.CreatePlaneController.show().url,
    "land" -> controllers.lands.routes.CreateLandController.show().url
  )

  private val allowedCreateRoutes: String => List[(String, String)] = elementType => elementCreateRoutes.dropWhile(_._1 != elementType).tail

  def createLinks(currentElementType: String)(implicit messages: Messages): TypedTag[String] = {
    div(cls := "dropdown show")(
      a(
        cls := "btn btn-success btn-block dropdown-toggle",
        href := "#",
        role := "button",
        id := "dropdownCreateElement",
        data("toggle") := "dropdown",
        aria.haspopup := "true",
        aria.expanded := "false"
      )(messages("select-element.create-element")),
      div(cls := "dropdown-menu btn-block", aria.labelledby := "dropdownCreateElement")(
        allowedCreateRoutes(currentElementType).map {
          case (elementType, url) => a(cls := "dropdown-item text-center", href := url)(messages(s"select-element.create-$elementType"))
        }
      )
    )
  }

}
