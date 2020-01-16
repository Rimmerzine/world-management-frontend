package views

import config.AppConfig
import controllers.routes
import play.api.i18n.Messages
import scalatags.Text
import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{nav, title}

trait MainTemplate {

  val appConfig: AppConfig

  val fullWidth: String = "12"
  val twoThirdsWidth: String = "8"

  private def pageHead(pageTitle: String)(implicit messages: Messages): TypedTag[String] = {
    head(
      title(messages(pageTitle)),
      meta(charset := "utf-8"),
      meta(charset := "viewport", content := "width=device-width, initial-scale=1, shrink-to-fit=no"),
      link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/bootstrap.css").url),
      link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/custom.css").url),
      script(src := routes.Assets.versioned("javascript/custom.js").url)
    )
  }

  private def topNavbar(implicit messages: Messages): TypedTag[String] = {
    nav(cls := "navbar navbar-expand-lg navbar-dark bg-dark")(
      div(cls := "navbar-brand")(messages("navbar.top.brand")),
      button(
        cls := "navbar-toggler",
        `type` := "button",
        data("toggle") := "collapse",
        data("target") := "#navbar-nav-top",
        aria.controls := "navbar-nav-bottom",
        aria.expanded := "false",
        aria.label := messages("navbar.bottom.toggle.aria")
      )(span(cls := "navbar-toggler-icon")),
      div(id := "navbar-nav-top", cls := "collapse navbar-collapse")(
        ul(cls := "navbar-nav")(
          li(cls := "nav-item")(
            a(cls := "nav-link", href := appConfig.homeLink)(messages("navbar.top.links.home"))
          ),
          li(cls := "nav-item dropdown")(
            a(
              cls := "nav-link dropdown-toggle",
              href := "#",
              id := "navbar-dropdown-menu-link-top",
              role := "button",
              data("toggle") := "dropdown",
              aria.haspopup := "true",
              aria.expanded := "false"
            )(messages("navbar.top.tools.dropdown")),
            div(cls := "dropdown-menu", aria.labelledby := "navbar-dropdown-menu-link-top")(
              a(cls := "dropdown-item", href := controllers.routes.ToolsController.show().url)(messages("navbar.top.tools.dropdown.tools"))
            )
          )
        )
      )
    )
  }

  private def innerContent(contentWidth: String, innerHtml: Text.Modifier*)(implicit messages: Messages): TypedTag[String] = {
    div(cls := "container-fluid")(
      div(cls := "row justify-content-center")(
        div(cls := "col-lg-10 full-content-body very-light-gray")(
          div(cls := "row justify-content-center")(
            div(cls := s"col-lg-$contentWidth")(
              innerHtml: _*
            )
          )
        )
      )
    )
  }

  private def bottomNavbar(implicit messages: Messages): TypedTag[String] = {
    nav(cls := "navbar navbar-expand-lg navbar-dark bg-dark")(
      div(cls := "navbar-brand")(messages("navbar.bottom.brand")),
      button(
        cls := "navbar-toggler",
        `type` := "button",
        data("toggle") := "collapse",
        data("target") := "#navbar-nav-bottom",
        aria.controls := "navbar-nav-bottom",
        aria.expanded := "false",
        aria.label := messages("navbar.bottom.toggle.aria")
      )(span(cls := "navbar-toggler-icon")),
      div(id := "navbar-nav-bottom", cls := "collapse navbar-collapse")(
        div(cls := "navbar-nav")(
          a(cls := "nav-item nav-link", target := "_blank", rel := "noreferer", href := appConfig.githubLink)(
            span(messages("navbar.bottom.github")),
            span(cls := "sr-only")(messages("common.opens-in-new-tab"))
          )
        )
      )
    )
  }

  def mainTemplate(pageTitle: String, contentWidth: String = fullWidth)(innerHtml: Text.Modifier*)(implicit messages: Messages): TypedTag[String] = {
    scalatags.Text.all.html(lang := "en")(
      pageHead(pageTitle),
      body(
        topNavbar,
        innerContent(contentWidth, innerHtml: _*),
        bottomNavbar,
        script(
          src := "https://code.jquery.com/jquery-3.3.1.slim.min.js",
          attr("integrity") := "sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo",
          attr("crossorigin") := "anonymous"
        ),
        script(
          src := "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js",
          attr("integrity") := "sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49",
          attr("crossorigin") := "anonymous"
        ),
        script(
          src := "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js",
          attr("integrity") := "sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy",
          attr("crossorigin") := "anonymous"
        )
      )
    )
  }


}
