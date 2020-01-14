package forms

import play.api.data.validation.{Constraint, Invalid, Valid}

import scala.util.Try

trait FormConstraints {

  def nonEmpty(error: ErrorWithArgs): Constraint[String] = Constraint { text: String =>
    if (text.nonEmpty) Valid else Invalid(error.key, error.args: _*)
  }

  def maxLength(maxLength: Int, error: ErrorWithArgs): Constraint[String] = Constraint { text: String =>
    if (text.length <= maxLength) Valid else Invalid(error.key, error.args: _*)
  }

  def validOption(options: List[String], error: ErrorWithArgs): Constraint[String] = Constraint { text: String =>
    if (options.contains(text)) Valid else Invalid(error.key, error.args: _*)
  }

  def isValidInteger(error: ErrorWithArgs): Constraint[String] = Constraint { text: String =>
    Try {
      text.toInt
      Valid
    }.getOrElse(Invalid(error.key, error.args: _*))
  }

  def isValidBoolean(error: ErrorWithArgs): Constraint[String] = Constraint { text: String =>
    Try {
      text.toBoolean
      Valid
    }.getOrElse(Invalid(error.key, error.args: _*))
  }

  def validRange(min: Int, max: Int, error: ErrorWithArgs): Constraint[Int] = Constraint { number: Int =>
    if (number >= min && number <= max) Valid else Invalid(error.key, error.args: _*)
  }

  def hasLength[T](length: Int, error: ErrorWithArgs): Constraint[List[T]] = Constraint { value: List[T] =>
    if (value.length == length) Valid else Invalid(error.key, error.args: _*)
  }

  def validHitPoints(error: ErrorWithArgs): Constraint[String] = Constraint { text =>

    val hitDiceRegex: String = "([1-9]{1}[0-9]*)d([1-9]{1}[0-9]*)((\\s?)\\+(\\s?)\\d+)?"
    val hitPointsRegex: String = "[1-9]{1}[0-9]*"

    if (text matches hitDiceRegex) {
      Valid
    } else if (text matches hitPointsRegex) {
      Valid
    } else {
      Invalid(error.key, error.args: _*)
    }
  }

  def minimumValue(minimum: Int, error: ErrorWithArgs): Constraint[Int] = Constraint { number: Int =>
    if (number >= minimum) Valid else Invalid(error.key, error.args: _*)
  }

}
