package forms

import play.api.data.validation.{Constraint, Invalid, Valid}

trait FormConstraints {

  def nonEmptyConstraint(error: String): Constraint[String] = Constraint { text: String =>
    if (text.nonEmpty) Valid else Invalid(error)
  }

  def maxLengthConstraint(maxLength: Int, error: String): Constraint[String] = Constraint { text: String =>
    if (text.length <= maxLength) Valid else Invalid(error)
  }

  def validOptionConstraint(options: List[String], error: String): Constraint[String] = Constraint { text: String =>
    if (options.contains(text)) Valid else Invalid(error)
  }

}
