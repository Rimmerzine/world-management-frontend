package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages

object SelectCreatureForm extends StopOnFirstFail with FormConstraints {

  val selectCreatureAlphabetically: String = "select-creature-alphabetically"
  val selectCreatureChallenge: String = "select-creature-challenge"

  def createNameStartDropdownOptions(possibleValues: List[String])(implicit messages: Messages): List[(String, String)] = {
    List("none" -> messages("select-creature.none")) ++ (possibleValues zip possibleValues.map(_.toUpperCase))
  }

  def createChallengeRatingsOptions(possibleValues: List[String])(implicit messages: Messages): List[(String, String)] = {
    List("none" -> messages("select-creature.none")) ++ (possibleValues zip possibleValues.map {
      case "0.125" => "⅛"
      case "0.25" => "¼"
      case "0.5" => "½"
      case other => other
    })
  }

  val selectCreatureForm: Form[(String, String)] = Form(
    tuple(
      selectCreatureAlphabetically -> default(text, ""),
      selectCreatureChallenge -> default(text, "")
    )
  )

}
