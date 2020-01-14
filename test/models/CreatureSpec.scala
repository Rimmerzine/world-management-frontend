package models

import helpers.UnitSpec
import play.api.libs.json._
import testutil.TestConstants

class CreatureSpec extends UnitSpec with TestConstants {

  "The Creature case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[Creature](creatureJson) mustBe JsSuccess(creature)
      }
      "the json is minimal" in {
        Json.fromJson[Creature](creatureMinimalJson) mustBe JsSuccess(creatureMinimal)
      }
    }

    "fail to read from json" when {
      List(
        "id",
        "detail"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureJson - key
          Json.fromJson[Creature](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The CreatureDetail case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[CreatureDetail](creatureDetailJson) mustBe JsSuccess(creatureDetail)
      }
      "the json is minimal" in {
        Json.fromJson[CreatureDetail](creatureDetailMinimalJson) mustBe JsSuccess(creatureDetailMinimal)
      }
    }

    "fail to read from json" when {
      List(
        "name",
        "size",
        "alignment",
        "armourClass",
        "hitPoints",
        "creatureType",
        "challengeRating",
        "typeTags",
        "movementSpeeds",
        "abilityScores",
        "skillProficiencies",
        "damageIntakes",
        "conditionImmunities",
        "senses",
        "languages",
        "creatureTraits",
        "actions",
        "legendaryActions"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureDetailJson - key
          Json.fromJson[CreatureDetail](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The MovementSpeeds case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[MovementSpeeds](creatureMovementSpeedsJson) mustBe JsSuccess(creatureMovementSpeeds)
      }
    }

    "fail to read from json" when {
      List(
        "basic",
        "burrow",
        "climb",
        "fly",
        "swim"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureMovementSpeedsJson - key
          Json.fromJson[MovementSpeeds](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The AbilityScore case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[AbilityScore](creatureAbilityScoreJson) mustBe JsSuccess(creatureAbilityScore)
      }
    }

    "fail to read from json" when {
      List(
        "score",
        "proficient"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureAbilityScoreJson - key
          Json.fromJson[AbilityScore](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The AbilityScores case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[AbilityScores](creatureAbilityScoresJson) mustBe JsSuccess(creatureAbilityScores)
      }
    }

    "fail to read from json" when {
      List(
        "strength",
        "dexterity",
        "constitution",
        "intelligence",
        "wisdom",
        "charisma"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureAbilityScoresJson - key
          Json.fromJson[AbilityScores](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The SkillProficiency case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[SkillProficiencies](creatureSkillProficienciesJson) mustBe JsSuccess(creatureSkillProficiencies)
      }
    }

    "fail to read from json" when {
      List(
        "animalHandling",
        "arcana",
        "athletics",
        "deception",
        "history",
        "insight",
        "intimidation",
        "investigation",
        "medicine",
        "nature",
        "perception",
        "persuasion",
        "religion",
        "slightOfHand",
        "stealth",
        "survival"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureSkillProficienciesJson - key
          Json.fromJson[SkillProficiencies](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The Sense case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[Senses](creatureSensesJson) mustBe JsSuccess(creatureSenses)
      }
    }

    "fail to read from json" when {
      List(
        "blindsight",
        "darkvision",
        "tremorsense",
        "truesight"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureSensesJson - key
          Json.fromJson[Senses](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The DamageIntake case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[DamageIntakes](creatureDamageIntakesJson) mustBe JsSuccess(creatureDamageIntakes)
      }
    }

    "fail to read from json" when {
      List(
        "bludgeoning",
        "piercing",
        "slashing",
        "acid",
        "cold",
        "fire",
        "force",
        "lightning",
        "necrotic",
        "poison",
        "psychic",
        "radiant",
        "thunder",
        "spells"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureDamageIntakesJson - key
          Json.fromJson[DamageIntakes](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The Trait case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[Trait](creatureTraitJson) mustBe JsSuccess(creatureTrait)
      }
    }

    "fail to read from json" when {
      List(
        "name",
        "description"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureTraitJson - key
          Json.fromJson[Trait](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The Action case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[Action](creatureActionJson) mustBe JsSuccess(creatureAction)
      }
    }

    "fail to read from json" when {
      List(
        "name",
        "description"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureActionJson - key
          Json.fromJson[Action](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }

  "The LegendaryAction case class" must {
    "read from json successfully" when {
      "the json is complete" in {
        Json.fromJson[LegendaryAction](creatureLegendaryActionJson) mustBe JsSuccess(creatureLegendaryAction)
      }
    }

    "fail to read from json" when {
      List(
        "name",
        "description"
      ) foreach { key =>
        s"$key is missing from the json" in {
          val json = creatureLegendaryActionJson - key
          Json.fromJson[LegendaryAction](json) mustBe JsError(JsPath \ key, "error.path.missing")
        }
      }
    }
  }


}
