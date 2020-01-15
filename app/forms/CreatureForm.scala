package forms

import forms.CustomFormats.{damageResistance, proficiencyLevel}
import models._
import play.api.data.Forms._
import play.api.data.{Form, Mapping}

object Sizes {

  val tiny: String = "tiny"
  val small: String = "small"
  val medium: String = "medium"
  val large: String = "large"
  val huge: String = "huge"
  val gargantuan: String = "gargantuan"

  val options: List[String] = List(
    tiny,
    small,
    medium,
    large,
    huge,
    gargantuan
  )

}

object Alignments {

  val lawfulGood: String = "lawful-good"
  val lawfulNeutral: String = "lawful-neutral"
  val lawfulEvil: String = "lawful-evil"
  val neutralGood: String = "neutral-good"
  val neutral: String = "neutral"
  val neutralEvil: String = "neutral-evil"
  val chaoticGood: String = "chaotic-good"
  val chaoticNeutral: String = "chaotic-neutral"
  val chaoticEvil: String = "chaotic-evil"
  val unaligned: String = "unaligned"
  val any: String = "any"

  val options: List[String] = List(
    lawfulGood,
    lawfulNeutral,
    lawfulEvil,
    neutralGood,
    neutral,
    neutralEvil,
    chaoticGood,
    chaoticNeutral,
    chaoticEvil,
    unaligned,
    any
  )

}

object Types {

  val aberration: String = "aberration"
  val beast: String = "beast"
  val celestial: String = "celestial"
  val construct: String = "construct"
  val dragon: String = "dragon"
  val elemental: String = "elemental"
  val fey: String = "fey"
  val fiend: String = "fiend"
  val giant: String = "giant"
  val humanoid: String = "humanoid"
  val monstrosity: String = "monstrosity"
  val ooze: String = "ooze"
  val plant: String = "plant"
  val undead: String = "undead"

  val options: List[String] = List(
    aberration,
    beast,
    celestial,
    construct,
    dragon,
    elemental,
    fey,
    fiend,
    giant,
    humanoid,
    monstrosity,
    ooze,
    plant,
    undead
  )

}

object Abilities {

  val strength: String = "strength"
  val dexterity: String = "dexterity"
  val constitution: String = "constitution"
  val intelligence: String = "intelligence"
  val wisdom: String = "wisdom"
  val charisma: String = "charisma"

  val keys: List[String] = List(strength, dexterity, constitution, intelligence, wisdom, charisma)

}

object Movements {

  val basic: String = "basic"
  val burrow: String = "burrow"
  val climb: String = "climb"
  val fly: String = "fly"
  val swim: String = "swim"

  val keys: List[String] = List(basic, burrow, climb, fly, swim)

}

object Skills {

  val animalHandling: String = "animal-handling"
  val arcana: String = "arcana"
  val athletics: String = "athletics"
  val deception: String = "deception"
  val history: String = "history"
  val insight: String = "insight"
  val intimidation: String = "intimidation"
  val investigation: String = "investigation"
  val medicine: String = "medicine"
  val nature: String = "nature"
  val perception: String = "perception"
  val performance: String = "performance"
  val persuasion: String = "persuasion"
  val religion: String = "religion"
  val slightOfHand: String = "slight-of-hand"
  val stealth: String = "stealth"
  val survival: String = "survival"

  val keys: List[String] = List(
    animalHandling,
    arcana,
    athletics,
    deception,
    history,
    insight,
    intimidation,
    investigation,
    medicine,
    nature,
    perception,
    persuasion,
    religion,
    slightOfHand,
    stealth,
    survival
  )

  val options: List[String] = List("none", "proficiency", "expertise")

}

object Damages {

  val bludgeoning: String = "bludgeoning"
  val piercing: String = "piercing"
  val slashing: String = "slashing"
  val acid: String = "acid"
  val cold: String = "cold"
  val fire: String = "fire"
  val force: String = "force"
  val lightning: String = "lightning"
  val necrotic: String = "necrotic"
  val poison: String = "poison"
  val psychic: String = "psychic"
  val radiant: String = "radiant"
  val thunder: String = "thunder"
  val spells: String = "spells"

  val keys: List[String] = List(
    bludgeoning,
    piercing,
    slashing,
    acid,
    cold,
    fire,
    force,
    lightning,
    necrotic,
    poison,
    psychic,
    radiant,
    thunder,
    spells
  )

  val options: List[String] = List("none", "vulnerable", "resistant", "immune")

}

object Conditions {

  val blinded: String = "blinded"
  val charmed: String = "charmed"
  val deafened: String = "deafened"
  val exhaustion: String = "exhaustion"
  val fatigued: String = "fatigued"
  val frightened: String = "frightened"
  val grappled: String = "grappled"
  val incapacitated: String = "incapacitated"
  val invisible: String = "invisible"
  val paralyzed: String = "paralyzed"
  val petrified: String = "petrified"
  val poisoned: String = "poisoned"
  val prone: String = "prone"
  val restrained: String = "restrained"
  val stunned: String = "stunned"
  val unconscious: String = "unconscious"

  val options: List[String] = List(
    blinded,
    charmed,
    deafened,
    exhaustion,
    fatigued,
    frightened,
    grappled,
    incapacitated,
    invisible,
    paralyzed,
    petrified,
    poisoned,
    prone,
    restrained,
    stunned,
    unconscious
  )

}

object Sense {

  val blindsight: String = "blindsight"
  val darkvision: String = "darkvision"
  val tremorsense: String = "tremorsense"
  val truesight: String = "truesight"

  val keys: List[String] = List(blindsight, darkvision, tremorsense, truesight)

}

object CreatureForm extends StopOnFirstFail with FormConstraints {

  val defaultText: Mapping[String] = default(text, "")

  val creatureName: String = "name"
  val nameRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.name.required")
  val nameTooLong: ErrorWithArgs = ErrorWithArgs("error.create-creature.name.max")
  val nameLengthMax: Int = 20
  val creatureNameMapping: Mapping[String] = defaultText.verifyingFirst(
    nonEmpty(nameRequired),
    maxLength(nameLengthMax, nameTooLong)
  )

  val creatureDescription: String = "description"
  val creatureDescriptionMapping: Mapping[Option[String]] = optional(text)

  val creatureSize: String = "size"
  val sizeRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.size.invalid")
  val creatureSizeMapping: Mapping[String] = defaultText.verifyingFirst(
    validOption(Sizes.options, sizeRequired)
  )

  val creatureAlignment: String = "alignment"
  val alignmentRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.alignment.invalid")
  val creatureAlignmentMapping: Mapping[String] = defaultText.verifyingFirst(
    validOption(Alignments.options, alignmentRequired)
  )

  val creatureArmourClass: String = "armour-class"
  val armourClassMinimum: Int = 1
  val armourClassMaximum: Int = 30
  val armourClassRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.armour-class.required")
  val armourClassNumeric: ErrorWithArgs = ErrorWithArgs("error.create-creature.armour-class.numeric")
  val armourClassOutOfRange: ErrorWithArgs = ErrorWithArgs("error.create-creature.armour-class.range")
  val creatureArmourClassMapping: Mapping[Int] = defaultText.verifyingFirst(
    nonEmpty(armourClassRequired),
    isValidInteger(armourClassNumeric)
  ).transform[Int](_.toInt, _.toString).verifying(
    validRange(armourClassMinimum, armourClassMaximum, armourClassOutOfRange)
  )

  val creatureHitPoints: String = "hit-points"
  val hitPointsRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.hit-points.required")
  val hitPointsInvalid: ErrorWithArgs = ErrorWithArgs("error.create-creature.hit-points.invalid")
  val creatureHitPointsMapping: Mapping[String] = defaultText.verifyingFirst(
    nonEmpty(hitPointsRequired),
    validHitPoints(hitPointsInvalid)
  )

  val creatureType: String = "type"
  val typeRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.type.invalid")
  val creatureTypeMapping: Mapping[String] = defaultText.verifyingFirst(
    validOption(Types.options, typeRequired)
  )

  val creatureChallengeRating: String = "challenge-rating"
  val challengeRatingRequired: ErrorWithArgs = ErrorWithArgs("error.create-creature.challenge-rating.invalid")
  val challengeRatingOptions: List[String] = (List(0.125, 0.25, 0.5) ++ (1 to 30)).map(_.toString)
  val creatureChallengeRatingMapping: Mapping[Double] = defaultText.verifyingFirst(
    validOption(challengeRatingOptions, challengeRatingRequired),
  ).transform[Double](_.toDouble, _.toString)

  val creatureTypeTag: String = "type-tag"
  val creatureTypeTagsMapping: Mapping[List[String]] = list(optional(text))
    .transform[List[String]](_.flatten, _.map(Some.apply))

  val movementSpeed: String = "movement-speed"
  val movementSpeedRequired: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.movement-speed.required", _)
  val movementSpeedNumeric: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.movement-speed.numeric", _)
  val movementSpeedMin: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.movement-speed.min", _)
  val movementSpeedsMapping: Mapping[MovementSpeeds] = {

    val movementSpeedKeyMapping: String => (String, Mapping[Int]) = { movementType: String =>
      movementType -> defaultText.verifyingFirst(
        nonEmpty(movementSpeedRequired(movementType)),
        isValidInteger(movementSpeedNumeric(movementType))
      ).transform[Int](_.toInt, _.toString).verifyingFirst(
        minimumValue(0, movementSpeedMin(movementType))
      )
    }

    mapping(
      movementSpeedKeyMapping(Movements.basic),
      movementSpeedKeyMapping(Movements.burrow),
      movementSpeedKeyMapping(Movements.climb),
      movementSpeedKeyMapping(Movements.fly),
      movementSpeedKeyMapping(Movements.swim)
    )(MovementSpeeds.apply)(MovementSpeeds.unapply)

  }

  val abilityScore: String = "ability-score"
  val abilityScoreValueMinimum: Int = 1
  val abilityScoreValueMaximum: Int = 30
  val abilityScoreValueRequired: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.ability-score.required", _)
  val abilityScoreValueNumeric: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.ability-score.numeric", _)
  val abilityScoreValueOutOfRange: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.ability-score.range", _)
  val abilityScoresMapping: Mapping[AbilityScores] = {

    val abilityScoreKeyMapping: String => (String, Mapping[AbilityScore]) = { ability =>
      ability -> mapping(
        "value" -> defaultText.verifyingFirst(
          nonEmpty(abilityScoreValueRequired(ability)),
          isValidInteger(abilityScoreValueNumeric(ability))
        ).transform[Int](_.toInt, _.toString).verifyingFirst(
          validRange(abilityScoreValueMinimum, abilityScoreValueMaximum, abilityScoreValueOutOfRange(ability))
        ),
        "proficient" -> boolean
      )(AbilityScore.apply)(AbilityScore.unapply)
    }

    mapping(
      abilityScoreKeyMapping(Abilities.strength),
      abilityScoreKeyMapping(Abilities.dexterity),
      abilityScoreKeyMapping(Abilities.constitution),
      abilityScoreKeyMapping(Abilities.intelligence),
      abilityScoreKeyMapping(Abilities.wisdom),
      abilityScoreKeyMapping(Abilities.charisma)
    )(AbilityScores.apply)(AbilityScores.unapply)

  }

  val skillProficiency: String = "skill-proficiency"
  val skillProficiencyMapping: Mapping[SkillProficiencies] = mapping(
    Skills.animalHandling -> proficiencyLevel,
    Skills.arcana -> proficiencyLevel,
    Skills.athletics -> proficiencyLevel,
    Skills.deception -> proficiencyLevel,
    Skills.history -> proficiencyLevel,
    Skills.insight -> proficiencyLevel,
    Skills.intimidation -> proficiencyLevel,
    Skills.investigation -> proficiencyLevel,
    Skills.medicine -> proficiencyLevel,
    Skills.nature -> proficiencyLevel,
    Skills.perception -> proficiencyLevel,
    Skills.persuasion -> proficiencyLevel,
    Skills.religion -> proficiencyLevel,
    Skills.slightOfHand -> proficiencyLevel,
    Skills.stealth -> proficiencyLevel,
    Skills.survival -> proficiencyLevel
  )(SkillProficiencies.apply)(SkillProficiencies.unapply)

  val damageIntake: String = "damage-intake"
  val damageIntakesMapping: Mapping[DamageIntakes] = mapping(
    Damages.bludgeoning -> damageResistance,
    Damages.piercing -> damageResistance,
    Damages.slashing -> damageResistance,
    Damages.acid -> damageResistance,
    Damages.cold -> damageResistance,
    Damages.fire -> damageResistance,
    Damages.force -> damageResistance,
    Damages.lightning -> damageResistance,
    Damages.necrotic -> damageResistance,
    Damages.poison -> damageResistance,
    Damages.psychic -> damageResistance,
    Damages.radiant -> damageResistance,
    Damages.thunder -> damageResistance,
    Damages.spells -> damageResistance
  )(DamageIntakes.apply)(DamageIntakes.unapply)

  val conditionImmunity: String = "condition-immunity"
  val conditionImmunityMapping: Mapping[List[String]] = list(
    text.verifyingFirst(
      validOption(Conditions.options, ErrorWithArgs("error.condition-immunity"))
    )
  ).transform[List[String]](_.distinct, identity)

  val sense: String = "sense"
  val senseRequired: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.sense.required", _)
  val senseNumber: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.sense.number", _)
  val senseMinimum: String => ErrorWithArgs = ErrorWithArgs("error.create-creature.sense.min", _)
  val sensesMapping: Mapping[Senses] = {

    val senseMapping: String => (String, Mapping[Int]) = { sense: String =>
      sense -> defaultText.verifyingFirst(
        nonEmpty(senseRequired(sense)),
        isValidInteger(senseNumber(sense)),
      ).transform[Int](_.toInt, _.toString).verifyingFirst(
        minimumValue(0, senseMinimum(sense))
      )
    }

    mapping(
      senseMapping(Sense.blindsight),
      senseMapping(Sense.darkvision),
      senseMapping(Sense.tremorsense),
      senseMapping(Sense.truesight)
    )(Senses.apply)(Senses.unapply)

  }

  val language: String = "language"
  val languagesMapping: Mapping[List[String]] = list(optional(text))
    .transform[List[String]](_.flatten, _.map(Some.apply))

  val creatureTrait: String = "trait"
  val traitName: String = "trait-name"
  val traitDescription: String = "trait-description"
  val creatureTraitsMapping: Mapping[List[Trait]] = list(
    mapping(
      traitName -> defaultText,
      traitDescription -> defaultText
    )(Trait.apply)(Trait.unapply)
  )

  val creatureAction: String = "action"
  val actionName: String = "action-name"
  val actionDescription: String = "action-description"
  val creatureActionMapping: Mapping[List[Action]] = list(
    mapping(
      actionName -> defaultText,
      actionDescription -> defaultText
    )(Action.apply)(Action.unapply)
  )

  val legendaryAction: String = "legendary-action"
  val legendaryActionName: String = "legendary-action-name"
  val legendaryActionDescription: String = "legendary-action-description"
  val legendaryActionMapping: Mapping[List[LegendaryAction]] = list(
    mapping(
      legendaryActionName -> defaultText,
      legendaryActionDescription -> defaultText
    )(LegendaryAction.apply)(LegendaryAction.unapply)
  )

  val creatureForm: Form[CreatureDetail] = Form(
    mapping(
      creatureName -> creatureNameMapping,
      creatureDescription -> creatureDescriptionMapping,
      creatureSize -> creatureSizeMapping,
      creatureAlignment -> creatureAlignmentMapping,
      creatureArmourClass -> creatureArmourClassMapping,
      creatureHitPoints -> creatureHitPointsMapping,
      creatureType -> creatureTypeMapping,
      creatureChallengeRating -> creatureChallengeRatingMapping,
      creatureTypeTag -> creatureTypeTagsMapping,
      movementSpeed -> movementSpeedsMapping,
      abilityScore -> abilityScoresMapping,
      skillProficiency -> skillProficiencyMapping,
      damageIntake -> damageIntakesMapping,
      conditionImmunity -> conditionImmunityMapping,
      sense -> sensesMapping,
      language -> languagesMapping,
      creatureTrait -> creatureTraitsMapping,
      creatureAction -> creatureActionMapping,
      legendaryAction -> legendaryActionMapping
    )(CreatureDetail.apply)(CreatureDetail.unapply)
  )

}
