package views.helpers

import play.api.mvc.Call
import scalatags.Text
import scalatags.Text.TypedTag
import scalatags.Text.all.{form => tagForm, _}

trait HtmlForm {

  def form(call: Call)(innerHtml: Text.Modifier*): TypedTag[String] = {
    tagForm(action := call.url, method := call.method)(
      innerHtml: _*
    )
  }

}
