package models

import java.util.UUID

import models.DamageIntakes._
import models.SkillProficiencies._
import play.api.libs.json._

case class Creature(id: String,
                    detail: CreatureDetail)

case class CreatureDetail(name: String,
                          description: Option[String],
                          size: String,
                          alignment: String,
                          armourClass: Int,
                          hitPoints: String,
                          creatureType: String,
                          challengeRating: Double,
                          typeTags: List[String],
                          movementSpeeds: MovementSpeeds,
                          abilityScores: AbilityScores,
                          skillProficiencies: SkillProficiencies,
                          damageIntakes: DamageIntakes,
                          conditionImmunities: List[String],
                          senses: Senses,
                          languages: List[String],
                          creatureTraits: List[Trait],
                          actions: List[Action],
                          legendaryActions: List[LegendaryAction])

case class MovementSpeeds(basic: Int, burrow: Int, climb: Int, fly: Int, swim: Int)

case class AbilityScores(strength: AbilityScore,
                         dexterity: AbilityScore,
                         constitution: AbilityScore,
                         intelligence: AbilityScore,
                         wisdom: AbilityScore,
                         charisma: AbilityScore)

case class AbilityScore(score: Int, proficient: Boolean)

case class SkillProficiencies(animalHandling: ProficiencyLevel,
                              arcana: ProficiencyLevel,
                              athletics: ProficiencyLevel,
                              deception: ProficiencyLevel,
                              history: ProficiencyLevel,
                              insight: ProficiencyLevel,
                              intimidation: ProficiencyLevel,
                              investigation: ProficiencyLevel,
                              medicine: ProficiencyLevel,
                              nature: ProficiencyLevel,
                              perception: ProficiencyLevel,
                              persuasion: ProficiencyLevel,
                              religion: ProficiencyLevel,
                              slightOfHand: ProficiencyLevel,
                              stealth: ProficiencyLevel,
                              survival: ProficiencyLevel)

case class Senses(blindsight: Int, darkvision: Int, tremorsense: Int, truesight: Int)

case class DamageIntakes(bludgeoning: DamageResistance,
                         piercing: DamageResistance,
                         slashing: DamageResistance,
                         acid: DamageResistance,
                         cold: DamageResistance,
                         fire: DamageResistance,
                         force: DamageResistance,
                         lightning: DamageResistance,
                         necrotic: DamageResistance,
                         poison: DamageResistance,
                         psychic: DamageResistance,
                         radiant: DamageResistance,
                         thunder: DamageResistance,
                         spells: DamageResistance)

case class Trait(name: String, description: String)

case class Action(name: String, description: String)

case class LegendaryAction(name: String, description: String)

object Creature {
  def create(creatureDetail: CreatureDetail): Creature = Creature(UUID.randomUUID().toString, creatureDetail)

  implicit lazy val format: OFormat[Creature] = Json.format[Creature]
}

object CreatureDetail {
  implicit lazy val format: OFormat[CreatureDetail] = Json.format[CreatureDetail]
}

object MovementSpeeds {
  implicit lazy val format: OFormat[MovementSpeeds] = Json.format[MovementSpeeds]
}

object AbilityScore {
  implicit lazy val format: OFormat[AbilityScore] = Json.format[AbilityScore]
}

object AbilityScores {
  implicit lazy val format: OFormat[AbilityScores] = Json.format[AbilityScores]
}

object SkillProficiencies {

  sealed trait ProficiencyLevel

  case object NoProficiency extends ProficiencyLevel {
    override val toString: String = "none"
  }

  case object Proficiency extends ProficiencyLevel {
    override val toString: String = "proficiency"
  }

  case object Expertise extends ProficiencyLevel {
    override val toString: String = "expertise"
  }

  implicit lazy val proficiencyLevelReads: Reads[ProficiencyLevel] = Reads[ProficiencyLevel] {
    case JsString(NoProficiency.toString) => JsSuccess(NoProficiency)
    case JsString(Proficiency.toString) => JsSuccess(Proficiency)
    case JsString(Expertise.toString) => JsSuccess(Expertise)
    case _ => JsError("error.proficiency-level.invalid")
  }

  implicit lazy val proficiencyLevelWrites: Writes[ProficiencyLevel] = Writes[ProficiencyLevel] {
    case NoProficiency => JsString(NoProficiency.toString)
    case Proficiency => JsString(Proficiency.toString)
    case Expertise => JsString(Expertise.toString)
  }

  implicit lazy val format: OFormat[SkillProficiencies] = Json.format[SkillProficiencies]

}

object Senses {
  implicit lazy val format: OFormat[Senses] = Json.format[Senses]
}

object DamageIntakes {

  sealed trait DamageResistance

  case object Vulnerable extends DamageResistance {
    override val toString: String = "vulnerable"
  }

  case object NoResistance extends DamageResistance {
    override val toString: String = "no-resistance"
  }

  case object Resistant extends DamageResistance {
    override val toString: String = "resistant"
  }

  case object Immune extends DamageResistance {
    override val toString: String = "immune"
  }

  implicit lazy val damageResistanceReads: Reads[DamageResistance] = Reads[DamageResistance] {
    case JsString(Vulnerable.toString) => JsSuccess(Vulnerable)
    case JsString(NoResistance.toString) => JsSuccess(NoResistance)
    case JsString(Resistant.toString) => JsSuccess(Resistant)
    case JsString(Immune.toString) => JsSuccess(Immune)
    case _ => JsError("error.damage-resistance.invalid")
  }

  implicit lazy val damageResistanceWrites: Writes[DamageResistance] = Writes[DamageResistance] {
    case Vulnerable => JsString(Vulnerable.toString)
    case NoResistance => JsString(NoResistance.toString)
    case Resistant => JsString(Resistant.toString)
    case Immune => JsString(Immune.toString)
  }

  implicit lazy val format: OFormat[DamageIntakes] = Json.format[DamageIntakes]

}

object Trait {
  implicit lazy val format: OFormat[Trait] = Json.format[Trait]
}

object Action {
  implicit lazy val format: OFormat[Action] = Json.format[Action]
}

object LegendaryAction {
  implicit lazy val format: OFormat[LegendaryAction] = Json.format[LegendaryAction]
}
