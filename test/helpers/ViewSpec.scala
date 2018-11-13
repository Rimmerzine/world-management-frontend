package helpers

trait ViewSpec extends UnitSpec {

  def titleHtml(message: String) = s"""<title id="title">${message.replace("&", "&amp;")}</title>"""
  def headerHtml(message: String) = s"""<h1 id="header">${message.replace("&", "&amp;")}</h1>"""
  def formHtml(action: String, method: String) = s""""<form action="$action" method="$method">"""

}