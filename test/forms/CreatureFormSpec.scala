package forms

import helpers.UnitSpec
import models.CreatureDetail
import models.DamageIntakes.Resistant
import models.SkillProficiencies.Proficiency
import play.api.data.{Form, FormError}
import testutil.TestConstants

class CreatureFormSpec extends UnitSpec with TestConstants {

  val creatureNameData: Map[String, String] = Map(CreatureForm.creatureName -> creatureName)
  val creatureDescriptionData: Map[String, String] = Map(CreatureForm.creatureDescription -> creatureDescription)
  val creatureSizeData: Map[String, String] = Map(CreatureForm.creatureSize -> creatureSize)
  val creatureAlignmentData: Map[String, String] = Map(CreatureForm.creatureAlignment -> creatureAlignment)
  val creatureArmourClassData: Map[String, String] = Map(CreatureForm.creatureArmourClass -> creatureArmourClass.toString)
  val creatureHitPointsData: Map[String, String] = Map(CreatureForm.creatureHitPoints -> creatureHitPoints)
  val creatureTypeData: Map[String, String] = Map(CreatureForm.creatureType -> creatureType)
  val creatureChallengeRatingData: Map[String, String] = Map(CreatureForm.creatureChallengeRating -> creatureChallengeRating.toString)
  val creatureTypeTagData: Map[String, String] = Map(s"${CreatureForm.creatureTypeTag}[0]" -> creatureTypeTag)

  val creatureMovementSpeedsData: Map[String, String] = Movements.keys.map(key =>
    s"${CreatureForm.movementSpeed}.$key" -> creatureMovementSpeedValue.toString
  ).toMap

  val creatureAbilityScoresData: Map[String, String] = Abilities.keys.flatMap(key =>
    List(
      s"${CreatureForm.abilityScore}.$key.value" -> creatureAbilityScoreValue.toString,
      s"${CreatureForm.abilityScore}.$key.proficient" -> creatureAbilityScoreProficient.toString
    )
  ).toMap

  val creatureAbilityScoresMinimalData: Map[String, String] = Abilities.keys.map(key =>
    s"${CreatureForm.abilityScore}.$key.value" -> creatureAbilityScoreValue.toString
  ).toMap

  val creatureSkillProficienciesData: Map[String, String] = Skills.keys.map(key =>
    s"${CreatureForm.skillProficiency}.$key" -> Proficiency.toString
  ).toMap

  val creatureDamageIntakesData: Map[String, String] = Damages.keys.map(key =>
    s"${CreatureForm.damageIntake}.$key" -> Resistant.toString
  ).toMap

  val creatureConditionImmunitiesData: Map[String, String] = Map(s"${CreatureForm.conditionImmunity}[0]" -> creatureConditionImmunity)

  val creatureSensesData: Map[String, String] = Sense.keys.map(key =>
    s"${CreatureForm.sense}.$key" -> creatureSenseValue.toString
  ).toMap

  val creatureLanguagesData: Map[String, String] = Map(s"${CreatureForm.language}[0]" -> creatureLanguage)

  val creatureTraitsData: Map[String, String] = Map(
    s"${CreatureForm.creatureTrait}[0].${CreatureForm.traitName}" -> creatureTraitName,
    s"${CreatureForm.creatureTrait}[0].${CreatureForm.traitDescription}" -> creatureTraitDescription
  )

  val creatureActionsData: Map[String, String] = Map(
    s"${CreatureForm.creatureAction}[0].${CreatureForm.actionName}" -> creatureActionName,
    s"${CreatureForm.creatureAction}[0].${CreatureForm.actionDescription}" -> creatureActionDescription
  )

  val creatureLegendaryActionsData: Map[String, String] = Map(
    s"${CreatureForm.legendaryAction}[0].${CreatureForm.legendaryActionName}" -> creatureLegendaryActionName,
    s"${CreatureForm.legendaryAction}[0].${CreatureForm.legendaryActionDescription}" -> creatureLegendaryActionDescription
  )

  val fullDataList: Map[String, String] = List(
    creatureNameData,
    creatureDescriptionData,
    creatureSizeData,
    creatureAlignmentData,
    creatureArmourClassData,
    creatureHitPointsData,
    creatureTypeData,
    creatureChallengeRatingData,
    creatureTypeTagData,
    creatureMovementSpeedsData,
    creatureAbilityScoresData,
    creatureSkillProficienciesData,
    creatureDamageIntakesData,
    creatureConditionImmunitiesData,
    creatureSensesData,
    creatureLanguagesData,
    creatureTraitsData,
    creatureActionsData,
    creatureLegendaryActionsData
  ).fold(Map.empty)(_ ++ _)

  val minimalDataList: Map[String, String] = List(
    creatureNameData,
    creatureSizeData,
    creatureAlignmentData,
    creatureArmourClassData,
    creatureHitPointsData,
    creatureTypeData,
    creatureChallengeRatingData,
    creatureMovementSpeedsData,
    creatureAbilityScoresMinimalData,
    creatureSensesData
  ).fold(Map.empty)(_ ++ _)

  val emptyDataList: Map[String, String] = Map.empty

  class ValidFormTest(data: Map[String, String], expected: CreatureDetail) {
    private val form: Form[CreatureDetail] = CreatureForm.creatureForm
    val boundForm: Form[CreatureDetail] = form.bind(data)

    boundForm.errors mustBe Seq.empty[FormError]
    boundForm.value mustBe Some(expected)
  }

  class InvalidFormTest(data: Map[String, String], errorKey: String, expectedError: ErrorWithArgs) {
    private val form: Form[CreatureDetail] = CreatureForm.creatureForm
    val boundForm: Form[CreatureDetail] = form.bind(data)

    boundForm.value mustBe None
    boundForm.errors mustBe List(FormError(errorKey, expectedError.key, expectedError.args))
  }

  "bind" must {
    "bind successfully" when {
      "valid data is provided" in new ValidFormTest(
        data = fullDataList,
        expected = creature.detail
      )
      "all optional data is not provided" in new ValidFormTest(
        data = minimalDataList,
        expected = creatureMinimal.detail
      )
    }
    "fail to bind" when {
      "name is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureName,
        errorKey = CreatureForm.creatureName,
        expectedError = CreatureForm.nameRequired
      )
      "name is empty" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureName -> ""),
        errorKey = CreatureForm.creatureName,
        expectedError = CreatureForm.nameRequired
      )
      "name is more than 20 characters" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureName -> "A" * (CreatureForm.nameLengthMax + 1)),
        errorKey = CreatureForm.creatureName,
        expectedError = CreatureForm.nameTooLong
      )

      "size is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureSize,
        errorKey = CreatureForm.creatureSize,
        expectedError = CreatureForm.sizeRequired
      )
      "size is not a valid option" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureSize -> "invalid"),
        errorKey = CreatureForm.creatureSize,
        expectedError = CreatureForm.sizeRequired
      )

      "alignment is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureAlignment,
        errorKey = CreatureForm.creatureAlignment,
        expectedError = CreatureForm.alignmentRequired
      )
      "alignment is not a valid option" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureAlignment -> "invalid"),
        errorKey = CreatureForm.creatureAlignment,
        expectedError = CreatureForm.alignmentRequired
      )

      "armour class is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureArmourClass,
        errorKey = CreatureForm.creatureArmourClass,
        expectedError = CreatureForm.armourClassRequired
      )
      "armour class is empty" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureArmourClass -> ""),
        errorKey = CreatureForm.creatureArmourClass,
        expectedError = CreatureForm.armourClassRequired
      )
      "armour class is not numeric" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureArmourClass -> "twenty"),
        errorKey = CreatureForm.creatureArmourClass,
        expectedError = CreatureForm.armourClassNumeric
      )
      s"armour class is below minimum" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureArmourClass -> (CreatureForm.armourClassMinimum - 1).toString),
        errorKey = CreatureForm.creatureArmourClass,
        expectedError = CreatureForm.armourClassOutOfRange
      )
      s"armour class is above maximum" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureArmourClass -> (CreatureForm.armourClassMaximum + 1).toString),
        errorKey = CreatureForm.creatureArmourClass,
        expectedError = CreatureForm.armourClassOutOfRange
      )

      "creature hit points is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureHitPoints,
        errorKey = CreatureForm.creatureHitPoints,
        expectedError = CreatureForm.hitPointsRequired
      )
      "creature hit points is empty" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureHitPoints -> ""),
        errorKey = CreatureForm.creatureHitPoints,
        expectedError = CreatureForm.hitPointsRequired
      )
      "creature hit points is not valid" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureHitPoints -> "invalid"),
        errorKey = CreatureForm.creatureHitPoints,
        expectedError = CreatureForm.hitPointsInvalid
      )

      "creature type is not provided" in new InvalidFormTest(
        data = fullDataList - CreatureForm.creatureType,
        errorKey = CreatureForm.creatureType,
        expectedError = CreatureForm.typeRequired
      )
      "creature type is not a valid option" in new InvalidFormTest(
        data = fullDataList + (CreatureForm.creatureType -> "invalid"),
        errorKey = CreatureForm.creatureType,
        expectedError = CreatureForm.typeRequired
      )

      Movements.keys foreach { key =>
        s"$key movement speed is not provided" in new InvalidFormTest(
          data = fullDataList - s"${CreatureForm.movementSpeed}.$key",
          errorKey = s"${CreatureForm.movementSpeed}.$key",
          expectedError = CreatureForm.movementSpeedRequired(key)
        )
        s"$key movement speed is empty" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.movementSpeed}.$key" -> ""),
          errorKey = s"${CreatureForm.movementSpeed}.$key",
          expectedError = CreatureForm.movementSpeedRequired(key)
        )
        s"$key movement speed is not numeric" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.movementSpeed}.$key" -> "twenty"),
          errorKey = s"${CreatureForm.movementSpeed}.$key",
          expectedError = CreatureForm.movementSpeedNumeric(key)
        )
        s"$key movement speed is below minimum" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.movementSpeed}.$key" -> (-1).toString),
          errorKey = s"${CreatureForm.movementSpeed}.$key",
          expectedError = CreatureForm.movementSpeedMin(key)
        )
      }

      Abilities.keys foreach { key =>
        s"$key ability score value is not provided" in new InvalidFormTest(
          data = fullDataList - s"${CreatureForm.abilityScore}.$key.value",
          errorKey = s"${CreatureForm.abilityScore}.$key.value",
          expectedError = CreatureForm.abilityScoreValueRequired(key)
        )
        s"$key ability score value is empty" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.abilityScore}.$key.value" -> ""),
          errorKey = s"${CreatureForm.abilityScore}.$key.value",
          expectedError = CreatureForm.abilityScoreValueRequired(key)
        )
        s"$key ability score value is not numeric" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.abilityScore}.$key.value" -> "twenty"),
          errorKey = s"${CreatureForm.abilityScore}.$key.value",
          expectedError = CreatureForm.abilityScoreValueNumeric(key)
        )
        s"$key ability score value is below minimum" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.abilityScore}.$key.value" -> (CreatureForm.abilityScoreValueMinimum - 1).toString),
          errorKey = s"${CreatureForm.abilityScore}.$key.value",
          expectedError = CreatureForm.abilityScoreValueOutOfRange(key)
        )
        s"$key ability score value is above maximum" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.abilityScore}.$key.value" -> (CreatureForm.abilityScoreValueMaximum + 1).toString),
          errorKey = s"${CreatureForm.abilityScore}.$key.value",
          expectedError = CreatureForm.abilityScoreValueOutOfRange(key)
        )

        s"$key ability score proficient is invalid" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.abilityScore}.$key.proficient" -> "invalid"),
          errorKey = s"${CreatureForm.abilityScore}.$key.proficient",
          expectedError = ErrorWithArgs("error.boolean")
        )
      }

      Skills.keys foreach { key =>
        s"$key skill selection is invalid" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.skillProficiency}.$key" -> "invalid"),
          errorKey = s"${CreatureForm.skillProficiency}.$key",
          expectedError = ErrorWithArgs("error.skill-proficiency")
        )
      }

      Damages.keys foreach { key =>
        s"$key damage selection is invalid" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.damageIntake}.$key" -> "invalid"),
          errorKey = s"${CreatureForm.damageIntake}.$key",
          expectedError = ErrorWithArgs("error.damage-resistance")
        )
      }

      "condition immunity is invalid" in new InvalidFormTest(
        data = fullDataList + (s"${CreatureForm.conditionImmunity}[0]" -> "invalid"),
        errorKey = s"${CreatureForm.conditionImmunity}[0]",
        expectedError = ErrorWithArgs("error.condition-immunity")
      )

      Sense.keys foreach { key =>
        s"$key sense value is not provided" in new InvalidFormTest(
          data = fullDataList - s"${CreatureForm.sense}.$key",
          errorKey = s"${CreatureForm.sense}.$key",
          expectedError = CreatureForm.senseRequired(key)
        )
        s"$key sense value is empty" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.sense}.$key" -> ""),
          errorKey = s"${CreatureForm.sense}.$key",
          expectedError = CreatureForm.senseRequired(key)
        )
        s"$key sense value is not numeric" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.sense}.$key" -> "twenty"),
          errorKey = s"${CreatureForm.sense}.$key",
          expectedError = CreatureForm.senseNumber(key)
        )
        s"$key sense value is below minimum" in new InvalidFormTest(
          data = fullDataList + (s"${CreatureForm.sense}.$key" -> (-1).toString),
          errorKey = s"${CreatureForm.sense}.$key",
          expectedError = CreatureForm.senseMinimum(key)
        )
      }

    }
  }

}
