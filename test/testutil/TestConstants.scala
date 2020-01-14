package testutil

import models.DamageIntakes.{NoResistance, Resistant}
import models.SkillProficiencies.{NoProficiency, Proficiency}
import models._
import play.api.libs.json.{JsObject, Json}
import scalatags.Text
import scalatags.Text.all.html

trait TestConstants {

  val emptyJson: JsObject = Json.obj()
  val emptyHtmlTag: Text.TypedTag[String] = html()

  val campaignId: String = "testCampaignId"
  val campaignName: String = "testCampaignName"
  val campaignDescription: String = "testCampaignDescription"
  val campaign: Campaign = Campaign("campaign", campaignId, campaignName, Some(campaignDescription), List.empty[WorldElement])

  val campaignJson: JsObject = Json.obj(
    "elementType" -> "campaign",
    "id" -> campaignId,
    "name" -> campaignName,
    "description" -> campaignDescription,
    "content" -> Json.arr()
  )

  val planeId: String = "testPlaneId"
  val planeName: String = "testPlaneName"
  val planeDescription: String = "testPlaneDescription"
  val planeAlignment: String = "lawful-good"
  val plane: Plane = Plane("plane", planeId, planeName, Some(planeDescription), List.empty[WorldElement], planeAlignment)
  val planeJson: JsObject = Json.obj(
    "elementType" -> "plane",
    "id" -> planeId,
    "name" -> planeName,
    "description" -> planeDescription,
    "content" -> Json.arr(),
    "alignment" -> planeAlignment
  )

  val landId: String = "testLandId"
  val landName: String = "testLandName"
  val landDescription: String = "testLandDescription"
  val land: Land = Land("land", landId, landName, Some(landDescription), List.empty[WorldElement])
  val landJson: JsObject = Json.obj(
    "elementType" -> "land",
    "id" -> landId,
    "name" -> landName,
    "description" -> landDescription,
    "content" -> Json.arr()
  )

  val campaignWithContent: Campaign = campaign.copy(content = List(plane))

  val creatureId: String = "testCreatureId"
  val creatureName: String = "testCreatureName"
  val creatureDescription: String = "testCreatureDescription"
  val creatureSize: String = "medium"
  val creatureAlignment: String = "lawful-good"
  val creatureArmourClass: Int = 18
  val creatureHitPoints: String = "18d8+54"
  var creatureType: String = "undead"
  val creatureChallengeRating: Int = 20
  val creatureTypeTag: String = "humanoid"

  val creatureMovementSpeedValue: Int = 30
  val creatureMovementSpeeds: MovementSpeeds = MovementSpeeds(
    basic = creatureMovementSpeedValue,
    burrow = creatureMovementSpeedValue,
    climb = creatureMovementSpeedValue,
    fly = creatureMovementSpeedValue,
    swim = creatureMovementSpeedValue
  )
  val creatureMovementSpeedsJson: JsObject = Json.obj(
    "basic" -> creatureMovementSpeedValue,
    "burrow" -> creatureMovementSpeedValue,
    "climb" -> creatureMovementSpeedValue,
    "fly" -> creatureMovementSpeedValue,
    "swim" -> creatureMovementSpeedValue
  )

  val creatureAbilityScoreValue: Int = 20
  val creatureAbilityScoreProficient: Boolean = true
  val creatureAbilityScore: AbilityScore = AbilityScore(creatureAbilityScoreValue, creatureAbilityScoreProficient)
  val creatureAbilityScoreJson: JsObject = Json.obj(
    "score" -> creatureAbilityScoreValue,
    "proficient" -> creatureAbilityScoreProficient
  )
  val creatureAbilityScoreNoProficiency: AbilityScore = AbilityScore(creatureAbilityScoreValue, !creatureAbilityScoreProficient)
  val creatureAbilityScoreNoProficiencyJson: JsObject = Json.obj(
    "score" -> creatureAbilityScoreValue,
    "proficient" -> !creatureAbilityScoreProficient
  )
  val creatureAbilityScores: AbilityScores = AbilityScores(
    strength = creatureAbilityScore,
    dexterity = creatureAbilityScore,
    constitution = creatureAbilityScore,
    intelligence = creatureAbilityScore,
    wisdom = creatureAbilityScore,
    charisma = creatureAbilityScore
  )
  val creatureAbilityScoresJson: JsObject = Json.obj(
    "strength" -> creatureAbilityScoreJson,
    "dexterity" -> creatureAbilityScoreJson,
    "constitution" -> creatureAbilityScoreJson,
    "intelligence" -> creatureAbilityScoreJson,
    "wisdom" -> creatureAbilityScoreJson,
    "charisma" -> creatureAbilityScoreJson
  )
  val creatureAbilityScoresNoProficiency: AbilityScores = AbilityScores(
    strength = creatureAbilityScoreNoProficiency,
    dexterity = creatureAbilityScoreNoProficiency,
    constitution = creatureAbilityScoreNoProficiency,
    intelligence = creatureAbilityScoreNoProficiency,
    wisdom = creatureAbilityScoreNoProficiency,
    charisma = creatureAbilityScoreNoProficiency
  )
  val creatureAbilityScoresNoProficiencyJson: JsObject = Json.obj(
    "strength" -> creatureAbilityScoreNoProficiencyJson,
    "dexterity" -> creatureAbilityScoreNoProficiencyJson,
    "constitution" -> creatureAbilityScoreNoProficiencyJson,
    "intelligence" -> creatureAbilityScoreNoProficiencyJson,
    "wisdom" -> creatureAbilityScoreNoProficiencyJson,
    "charisma" -> creatureAbilityScoreNoProficiencyJson
  )

  val creatureSkillProficiencies: SkillProficiencies = SkillProficiencies(
    animalHandling = Proficiency,
    arcana = Proficiency,
    athletics = Proficiency,
    deception = Proficiency,
    history = Proficiency,
    insight = Proficiency,
    intimidation = Proficiency,
    investigation = Proficiency,
    medicine = Proficiency,
    nature = Proficiency,
    perception = Proficiency,
    persuasion = Proficiency,
    religion = Proficiency,
    slightOfHand = Proficiency,
    stealth = Proficiency,
    survival = Proficiency
  )
  val creatureSkillProficienciesJson: JsObject = Json.obj(
    "animalHandling" -> Proficiency,
    "arcana" -> Proficiency,
    "athletics" -> Proficiency,
    "deception" -> Proficiency,
    "history" -> Proficiency,
    "insight" -> Proficiency,
    "intimidation" -> Proficiency,
    "investigation" -> Proficiency,
    "medicine" -> Proficiency,
    "nature" -> Proficiency,
    "perception" -> Proficiency,
    "persuasion" -> Proficiency,
    "religion" -> Proficiency,
    "slightOfHand" -> Proficiency,
    "stealth" -> Proficiency,
    "survival" -> Proficiency
  )
  val creatureSkillProficienciesNone: SkillProficiencies = SkillProficiencies(
    animalHandling = NoProficiency,
    arcana = NoProficiency,
    athletics = NoProficiency,
    deception = NoProficiency,
    history = NoProficiency,
    insight = NoProficiency,
    intimidation = NoProficiency,
    investigation = NoProficiency,
    medicine = NoProficiency,
    nature = NoProficiency,
    perception = NoProficiency,
    persuasion = NoProficiency,
    religion = NoProficiency,
    slightOfHand = NoProficiency,
    stealth = NoProficiency,
    survival = NoProficiency
  )
  val creatureSkillProficienciesNoneJson: JsObject = Json.obj(
    "animalHandling" -> NoProficiency,
    "arcana" -> NoProficiency,
    "athletics" -> NoProficiency,
    "deception" -> NoProficiency,
    "history" -> NoProficiency,
    "insight" -> NoProficiency,
    "intimidation" -> NoProficiency,
    "investigation" -> NoProficiency,
    "medicine" -> NoProficiency,
    "nature" -> NoProficiency,
    "perception" -> NoProficiency,
    "persuasion" -> NoProficiency,
    "religion" -> NoProficiency,
    "slightOfHand" -> NoProficiency,
    "stealth" -> NoProficiency,
    "survival" -> NoProficiency
  )

  val creatureDamageIntakes: DamageIntakes = DamageIntakes(
    bludgeoning = Resistant,
    piercing = Resistant,
    slashing = Resistant,
    acid = Resistant,
    cold = Resistant,
    fire = Resistant,
    force = Resistant,
    lightning = Resistant,
    necrotic = Resistant,
    poison = Resistant,
    psychic = Resistant,
    radiant = Resistant,
    thunder = Resistant,
    spells = Resistant
  )
  val creatureDamageIntakesJson: JsObject = Json.obj(
    "bludgeoning" -> Resistant,
    "piercing" -> Resistant,
    "slashing" -> Resistant,
    "acid" -> Resistant,
    "cold" -> Resistant,
    "fire" -> Resistant,
    "force" -> Resistant,
    "lightning" -> Resistant,
    "necrotic" -> Resistant,
    "poison" -> Resistant,
    "psychic" -> Resistant,
    "radiant" -> Resistant,
    "thunder" -> Resistant,
    "spells" -> Resistant
  )
  val creatureDamageIntakesNoResistance: DamageIntakes = DamageIntakes(
    bludgeoning = NoResistance,
    piercing = NoResistance,
    slashing = NoResistance,
    acid = NoResistance,
    cold = NoResistance,
    fire = NoResistance,
    force = NoResistance,
    lightning = NoResistance,
    necrotic = NoResistance,
    poison = NoResistance,
    psychic = NoResistance,
    radiant = NoResistance,
    thunder = NoResistance,
    spells = NoResistance
  )
  val creatureDamageIntakesNoResistanceJson: JsObject = Json.obj(
    "bludgeoning" -> NoResistance,
    "piercing" -> NoResistance,
    "slashing" -> NoResistance,
    "acid" -> NoResistance,
    "cold" -> NoResistance,
    "fire" -> NoResistance,
    "force" -> NoResistance,
    "lightning" -> NoResistance,
    "necrotic" -> NoResistance,
    "poison" -> NoResistance,
    "psychic" -> NoResistance,
    "radiant" -> NoResistance,
    "thunder" -> NoResistance,
    "spells" -> NoResistance
  )

  val creatureConditionImmunity: String = "unconscious"

  val creatureSenseValue: Int = 60
  val creatureSenses: Senses = Senses(
    blindsight = creatureSenseValue,
    darkvision = creatureSenseValue,
    tremorsense = creatureSenseValue,
    truesight = creatureSenseValue
  )
  val creatureSensesJson: JsObject = Json.obj(
    "blindsight" -> creatureSenseValue,
    "darkvision" -> creatureSenseValue,
    "tremorsense" -> creatureSenseValue,
    "truesight" -> creatureSenseValue
  )

  val creatureLanguage: String = "testCreatureLanguage"

  val creatureTraitName: String = "testCreatureTraitName"
  val creatureTraitDescription: String = "testCreatureTraitDescription"
  val creatureTrait: Trait = Trait(creatureTraitName, creatureTraitDescription)
  val creatureTraitJson: JsObject = Json.obj(
    "name" -> creatureTraitName,
    "description" -> creatureTraitDescription
  )

  val creatureActionName: String = "testCreatureActionName"
  val creatureActionDescription: String = "testCreatureActionDescription"
  val creatureAction: Action = Action(creatureActionName, creatureActionDescription)
  val creatureActionJson: JsObject = Json.obj(
    "name" -> creatureActionName,
    "description" -> creatureActionDescription
  )

  val creatureLegendaryActionName: String = "testCreatureLegendaryActionName"
  val creatureLegendaryActionDescription: String = "testCreatureLegendaryActionDescription"
  val creatureLegendaryAction: LegendaryAction = LegendaryAction(creatureLegendaryActionName, creatureLegendaryActionDescription)
  val creatureLegendaryActionJson: JsObject = Json.obj(
    "name" -> creatureLegendaryActionName,
    "description" -> creatureLegendaryActionDescription
  )

  val creatureDetail: CreatureDetail = CreatureDetail(
    name = creatureName,
    description = Some(creatureDescription),
    size = creatureSize,
    alignment = creatureAlignment,
    armourClass = creatureArmourClass,
    hitPoints = creatureHitPoints,
    creatureType = creatureType,
    challengeRating = creatureChallengeRating,
    typeTags = List(creatureTypeTag),
    movementSpeeds = creatureMovementSpeeds,
    abilityScores = creatureAbilityScores,
    skillProficiencies = creatureSkillProficiencies,
    damageIntakes = creatureDamageIntakes,
    conditionImmunities = List(creatureConditionImmunity),
    senses = creatureSenses,
    languages = List(creatureLanguage),
    creatureTraits = List(creatureTrait),
    actions = List(creatureAction),
    legendaryActions = List(creatureLegendaryAction)
  )

  val creatureDetailJson: JsObject = Json.obj(
    "name" -> creatureName,
    "description" -> creatureDescription,
    "size" -> creatureSize,
    "alignment" -> creatureAlignment,
    "armourClass" -> creatureArmourClass,
    "hitPoints" -> creatureHitPoints,
    "creatureType" -> creatureType,
    "challengeRating" -> creatureChallengeRating,
    "typeTags" -> Json.arr(creatureTypeTag),
    "movementSpeeds" -> creatureMovementSpeedsJson,
    "abilityScores" -> creatureAbilityScoresJson,
    "skillProficiencies" -> creatureSkillProficienciesJson,
    "damageIntakes" -> creatureDamageIntakesJson,
    "conditionImmunities" -> Json.arr(creatureConditionImmunity),
    "senses" -> creatureSensesJson,
    "languages" -> Json.arr(creatureLanguage),
    "creatureTraits" -> Json.arr(creatureTraitJson),
    "actions" -> Json.arr(creatureActionJson),
    "legendaryActions" -> Json.arr(creatureLegendaryActionJson)
  )

  val creature: Creature = Creature(
    id = creatureId,
    detail = creatureDetail
  )

  val creatureJson: JsObject = Json.obj(
    "id" -> creatureId,
    "detail" -> creatureDetailJson
  )

  val creatureDetailMinimal: CreatureDetail = CreatureDetail(
    name = creatureName,
    description = None,
    size = creatureSize,
    alignment = creatureAlignment,
    armourClass = creatureArmourClass,
    hitPoints = creatureHitPoints,
    creatureType = creatureType,
    challengeRating = creatureChallengeRating,
    typeTags = List(),
    movementSpeeds = creatureMovementSpeeds,
    abilityScores = creatureAbilityScoresNoProficiency,
    skillProficiencies = creatureSkillProficienciesNone,
    damageIntakes = creatureDamageIntakesNoResistance,
    conditionImmunities = List(),
    senses = creatureSenses,
    languages = List(),
    creatureTraits = List(),
    actions = List(),
    legendaryActions = List()
  )

  val creatureDetailMinimalJson: JsObject = Json.obj(
    "name" -> creatureName,
    "size" -> creatureSize,
    "alignment" -> creatureAlignment,
    "armourClass" -> creatureArmourClass,
    "hitPoints" -> creatureHitPoints,
    "creatureType" -> creatureType,
    "challengeRating" -> creatureChallengeRating,
    "typeTags" -> Json.arr(),
    "movementSpeeds" -> creatureMovementSpeedsJson,
    "abilityScores" -> creatureAbilityScoresNoProficiencyJson,
    "skillProficiencies" -> creatureSkillProficienciesNoneJson,
    "damageIntakes" -> creatureDamageIntakesNoResistanceJson,
    "conditionImmunities" -> Json.arr(),
    "senses" -> creatureSensesJson,
    "languages" -> Json.arr(),
    "creatureTraits" -> Json.arr(),
    "actions" -> Json.arr(),
    "legendaryActions" -> Json.arr()
  )

  val creatureMinimal: Creature = Creature(
    id = creatureId,
    detail = creatureDetailMinimal
  )

  val creatureMinimalJson: JsObject = Json.obj(
    "id" -> creatureId,
    "detail" -> creatureDetailMinimalJson
  )

}
