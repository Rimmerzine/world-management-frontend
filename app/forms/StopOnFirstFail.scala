package forms

import play.api.data.Mapping
import play.api.data.validation.{Constraint, Valid, ValidationResult}

import scala.annotation.tailrec

trait StopOnFirstFail {

  implicit class StopOnFirstFailVerify[T](mapping: Mapping[T]) {
    def verifyingFirst(constraints: Constraint[T]*): Mapping[T] = {
      mapping.verifying(stopOnFirstFail(constraints: _*))
    }
  }

  def stopOnFirstFail[A](constraints: Constraint[A]*): Constraint[A] = Constraint { field: A =>

    @tailrec
    def constraintValidator(field: A, constraints: Constraint[A]*): ValidationResult = {
      constraints.toList match {
        case Nil => Valid
        case head :: tail =>
          head(field) match {
            case Valid => constraintValidator(field, tail: _*)
            case invalid@_ => invalid
          }
      }
    }

    constraintValidator(field, constraints: _*)

  }

}
