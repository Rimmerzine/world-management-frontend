package forms

import models.DamageIntakes._
import models.SkillProficiencies._
import play.api.data.Forms.of
import play.api.data.format.Formatter
import play.api.data.{FormError, Mapping}

object CustomFormats {

  val proficiencyLevelFormatter: Formatter[ProficiencyLevel] = new Formatter[ProficiencyLevel] {

    val noProficiency: String = NoProficiency.toString
    val proficiency: String = Proficiency.toString
    val expertise: String = Expertise.toString

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], ProficiencyLevel] = Right(data.getOrElse(key, noProficiency)).right.flatMap {
      case `noProficiency` => Right(NoProficiency)
      case `proficiency` => Right(Proficiency)
      case `expertise` => Right(Expertise)
      case _ => Left(Seq(FormError(key, "error.skill-proficiency", Nil)))
    }

    def unbind(key: String, value: ProficiencyLevel): Map[String, String] = Map(key -> value.toString)
  }

  val damageResistanceFormatter: Formatter[DamageResistance] = new Formatter[DamageResistance] {

    val vulnerable: String = Vulnerable.toString
    val noResistance: String = NoResistance.toString
    val resistance: String = Resistant.toString
    val immune: String = Immune.toString

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], DamageResistance] = Right(data.getOrElse(key, noResistance)).right.flatMap {
      case `vulnerable` => Right(Vulnerable)
      case `noResistance` => Right(NoResistance)
      case `resistance` => Right(Resistant)
      case `immune` => Right(Immune)
      case _ => Left(Seq(FormError(key, "error.damage-resistance", Nil)))
    }

    def unbind(key: String, value: DamageResistance): Map[String, String] = Map(key -> value.toString)

  }

  val proficiencyLevel: Mapping[ProficiencyLevel] = of[ProficiencyLevel](proficiencyLevelFormatter)

  val damageResistance: Mapping[DamageResistance] = of[DamageResistance](damageResistanceFormatter)

}
