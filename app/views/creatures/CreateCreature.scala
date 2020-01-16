package views.creatures

import config.AppConfig
import forms.CreatureForm._
import forms._
import javax.inject.Inject
import models.CreatureDetail
import play.api.data.{Field, Form}
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.{details, summary}
import views.MainTemplate
import views.helpers.HtmlForm
import views.helpers.inputs._

class CreateCreatureImpl @Inject()(val appConfig: AppConfig) extends CreateCreature

trait CreateCreature extends MainTemplate
  with InputTextWithLabel
  with TextAreaWithLabel
  with InputTextNoLabel
  with CheckboxWithLabel
  with RadioButtonNoLabel
  with DropdownWithLabel
  with HtmlForm {

  def multipleInput(field: Field)(inputField: Field => TypedTag[String]): Seq[TypedTag[String]] = {

    val indexes: Seq[Int] = field.indexes match {
      case Nil => 0 until 1
      case other => other
    }

    indexes.map(index => inputField(field(s"[$index]")))

  }

  private def creatureNameDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group")(
      inputTextWithLabel(creatureForm(creatureName), messages("create-creature.name"), Some(messages("create-creature.name-aria")))
    )
  }

  private def creatureDescriptionDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group")(
      textAreaWithLabel(creatureForm(creatureDescription), messages("create-creature.description"), Some(messages("create-creature.description-aria")))
    )
  }

  private def creatureSizeDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    val options: List[(String, String)] = Sizes.options.map(size => (size, messages(s"size.$size")))
    div(cls := "col-xl-4 form-group")(
      dropdownWithLabel(creatureForm(creatureSize), options, messages("create-creature.size"), Some(messages("create-creature.size-aria")), includeSelectOption = true)
    )
  }

  private def creatureAlignmentDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    val options: List[(String, String)] = Alignments.options.map(alignment => (alignment, messages(s"alignment.$alignment")))
    div(cls := "col-xl-4 form-group")(
      dropdownWithLabel(creatureForm(creatureAlignment), options, messages("create-creature.alignment"), Some(messages("create-creature.alignment-aria")), includeSelectOption = true)
    )
  }

  private def creatureArmourClassDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "col-xl-4 form-group")(
      inputTextWithLabel(creatureForm(creatureArmourClass), messages("create-creature.armour-class"), Some(messages("create-creature.armour-class-aria")))
    )
  }

  private def creatureHitPointsDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "col-xl-4 form-group")(
      inputTextWithLabel(creatureForm(creatureHitPoints), messages("create-creature.hit-points"), Some(messages("create-creature.hit-points-aria")))
    )
  }

  private def creatureChallengeRatingDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "col-xl-4 form-group")(
      inputTextWithLabel(creatureForm(creatureChallengeRating), messages("create-creature.challenge-rating"), Some(messages("create-creature.challenge-rating-aria")))
    )
  }

  private def creatureTypeDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    val options: List[(String, String)] = Types.options.map(creatureType => (creatureType, messages(s"type.$creatureType")))
    div(cls := "col-xl-4 form-group")(
      dropdownWithLabel(creatureForm(creatureType), options, messages("create-creature.type"), Some(messages("create-creature.type-aria")), includeSelectOption = true)
    )
  }

  private def creatureTypeTagsDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "type-tags-heading")(
      h3(cls := "header-medium", id := "type-tags-heading")(messages("create-creature.type-tag.heading")),
      details(
        summary(role := "button", aria.controls := "type-tags-content")(messages("create-creature.type-tag-details.summary")),
        div(id := "type-tags-content")(messages("create-creature.type-tag-details.content"))
      ),
      div(cls := "row", id := "tag-group")(
        multipleInput(creatureForm(creatureTypeTag))(field =>
          div(cls := "col-xl-4 form-group")(
            inputTextNoLabel(field, Some(messages("create-creature.type-tag-input.aria")))
          )
        )
      ),
      button(`type` := "button", cls := "btn btn-success", id := "add-type-tag-button", onclick := "addTypeTag()")(
        messages("create-creature.type-tag.add")
      ),
      button(`type` := "button", cls := "btn btn-success", id := "remove-type-tag-button", onclick := "removeTypeTag()")(
        messages("create-creature.type-tag.remove")
      )
    )
  }

  private def creatureMovementSpeedDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(role := "group", aria.labelledby := "speed-heading")(
      h3(cls := "header-medium", id := "speed-heading", aria.label := "create-creature.speed.heading-aria")(messages("create-creature.speed.heading")),
      div(cls := "row")(
        Movements.keys map { key =>
          div(cls := "col-xl-4 form-group")(
            inputTextWithLabel(
              creatureForm(s"$movementSpeed.$key"),
              messages(s"create-creature.speed.$key"),
              Some(messages(s"create-creature.speed.$key-aria"))
            )
          )
        }
      )
    )
  }

  private def creatureAbilityScoreDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(role := "group", aria.labelledby := "ability-scores-heading")(
      h3(cls := "header-medium", id := "ability-scores-heading")(messages("create-creature.ability-scores.heading")),
      div(cls := "row")(
        Abilities.keys map { key =>
          div(cls := "col-xl-4 form-group")(
            inputTextWithLabel(
              creatureForm(s"$abilityScore.$key.value"),
              messages(s"create-creature.ability-scores.$key"),
              Some(messages(s"create-creature.ability-scores.$key-aria"))
            )
          )
        }
      )
    )
  }

  private def creatureSavingThrowDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "saving-throws-heading")(
      h3(cls := "header-medium", id := "saving-throws-heading")(messages("create-creature.saving-throws.heading")),
      div(cls := "row")(
        Abilities.keys map { key =>
          div(cls := "col-xl-4")(
            div(cls := "form-check")(
              checkboxWithLabel(
                creatureForm(s"$abilityScore.$key.proficient"),
                messages(s"create-creature.saving-throws.$key"),
                Some(messages(s"create-creature.saving-throws.$key-aria"))
              )
            )
          )
        }
      )
    )
  }

  private def creatureSkillProficiencyDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "skill-proficiency-heading")(
      h3(cls := "header-medium", id := "skill-proficiency-heading")(messages("create-creature.skill-proficiency.heading")),
      div(cls := "row bold table-row")(
        div(cls := "col-3"),
        Skills.options map { option =>
          div(cls := "col-2", aria.hidden := "true")(messages(s"create-creature.skill-proficiency.$option"))
        }
      ),
      Skills.keys map { key =>
        div(cls := "row table-row")(
          div(cls := "col-3 bold")(messages(s"create-creature.skill-proficiency.$key")),
          Skills.options map { option =>
            div(cls := "col-2")(
              div(cls := "form-check")(
                radioButtonNoLabel(
                  creatureForm(s"$skillProficiency.$key"),
                  option,
                  Some(messages(s"create-creature.skill-proficiency.$option-aria", messages(s"create-creature.skill-proficiency.$key"))))
              )
            )
          }
        )
      }
    )
  }

  def creatureResistanceDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "resistances-heading")(
      h3(cls := "header-medium", id := "resistances-heading")(messages("create-creature.resistances.heading")),
      div(cls := "row bold table-row")(
        div(cls := "col-3"),
        Damages.options map { option =>
          div(cls := "col-2", aria.hidden := "true")(messages(s"create-creature.resistances.$option"))
        }
      ),
      Damages.keys map { key =>
        div(cls := "row table-row")(
          div(cls := "col-3 bold")(messages(s"create-creature.resistances.$key")),
          Damages.options map { option =>
            div(cls := "col-2")(
              div(cls := "form-check")(
                radioButtonNoLabel(
                  creatureForm(s"$damageIntake.$key"),
                  option,
                  Some(messages(s"create-creature.resistances.$option-aria", messages(s"create-creature.resistances.$key")))
                )
              )
            )
          }
        )
      }
    )
  }

  private def creatureConditionDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "conditions-heading")(
      h3(cls := "header-medium", id := "conditions-heading")(messages("create-creature.conditions.heading")),
      div(cls := "row")(
        Conditions.options map { option =>
          div(cls := "col-xl-3")(
            div(cls := "form-check")(
              checkboxWithLabel(
                creatureForm(s"$conditionImmunity[$option]"),
                messages(s"create-creature.conditions.$option"),
                Some(messages(s"create-creature.conditions.option-aria", messages(s"create-creature.conditions.$option")))
              )
            )
          )
        }
      )
    )
  }

  private def creatureSenseDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(role := "group", aria.labelledby := "senses-heading")(
      h3(cls := "header-medium", id := "senses-heading", aria.label := messages("create-creature.senses.heading-aria"))(
        messages("create-creature.senses.heading")
      ),
      div(cls := "row")(
        Sense.keys map { key =>
          div(cls := "col-xl-3 form-group")(
            inputTextWithLabel(
              creatureForm(s"$sense.$key"),
              messages(s"create-creature.senses.$key"),
              Some(messages("create-creature.senses.sense-aria", messages(s"create-creature.senses.$key")))
            )
          )
        }
      )
    )
  }

  private def creatureLanguageDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "languages-heading")(
      h3(cls := "header-medium", id := "languages-heading")(messages("create-creature.languages.heading")),
      div(cls := "row", id := "language-group")(
        multipleInput(creatureForm(language))(field =>
          div(cls := "col-xl-4 form-group")(
            inputTextNoLabel(field, Some(messages("create-creature.languages.input-aria")))
          )
        )
      ),
      button(cls := "btn btn-success", `type` := "button", id := "add-language-button", onclick := "addLanguage()")(
        messages("create-creature.languages.add")
      ),
      button(cls := "btn btn-success", `type` := "button", id := "remove-language-button", onclick := "removeLanguage()")(
        messages("create-creature.languages.remove")
      )
    )
  }

  private def creatureTraitDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {

    val traitNames = multipleInput(creatureForm(traitName))(field =>
      div(cls := "col-xl-4 form-group")(
        inputTextWithLabel(
          field,
          messages("create-creature.traits.name"),
          Some(messages("create-creature.traits.name-aria"))
        )
      )
    )

    val traitDescriptions = multipleInput(creatureForm(traitDescription))(field =>
      div(cls := "col-xl-4 form-group")(
        inputTextWithLabel(
          field,
          messages("create-creature.traits.description"),
          Some(messages("create-creature.traits.description-aria"))
        )
      )
    )


    div(cls := "form-group", role := "group", aria.labelledby := "traits-heading")(
      h3(cls := "header-medium", id := "traits-heading")(messages("create-creature.traits.heading")),
      div(id := "trait-group")(
        (traitNames zip traitDescriptions) map { case (nameBlock, descriptionBlock) =>
          div(cls := "row")(
            Seq(nameBlock, descriptionBlock)
          )
        }
      ),
      button(cls := "btn btn-success", `type` := "button", id := "add-trait-button", onclick := "addTrait()")(
        messages("create-creature.traits.add")
      ),
      button(cls := "btn btn-success", `type` := "button", id := "remove-trait-button", onclick := "removeTrait()")(
        messages("create-creature.traits.remove")
      )
    )
  }

  private def creatureActionDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "actions-heading")(
      h3(cls := "header-medium", id := "actions-heading")(messages("create-creature.actions.heading")),
      div(id := "action-group")(
        div(cls := "row")(
          div(cls := "col-xl-4 form-group")(
            inputTextWithLabel(
              creatureForm(actionName),
              messages("create-creature.actions.name"),
              Some(messages("create-creature.actions.name-aria"))
            )
          ),
          div(cls := "col-xl-8 form-group")(
            textAreaWithLabel(
              creatureForm(actionDescription),
              messages("create-creature.actions.description"),
              Some(messages("create-creature.actions.description-aria"))
            )
          )
        )
      ),
      button(cls := "btn btn-success", `type` := "button", id := "add-action-button", onclick := "addAction()")(
        messages("create-creature.actions.add")
      ),
      button(cls := "btn btn-success", `type` := "button", id := "remove-action-button", onclick := "removeAction()")(
        messages("create-creature.actions.remove")
      )
    )
  }

  private def creatureLegendaryActionDisplay(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    div(cls := "form-group", role := "group", aria.labelledby := "legendary-actions-heading")(
      h3(cls := "header-medium", id := "legendary-actions-heading")(messages("create-creature.legendary-actions.heading")),
      div(id := "legendary-action-group")(
        div(cls := "row")(
          div(cls := "col-xl-4 form-group")(
            inputTextWithLabel(
              creatureForm(legendaryActionName),
              messages("create-creature.legendary-actions.name"),
              Some(messages("create-creature.legendary-actions.name-aria"))
            )
          ),
          div(cls := "col-xl-8 form-group")(
            textAreaWithLabel(
              creatureForm(legendaryActionDescription),
              messages("create-creature.legendary-actions.description"),
              Some(messages("create-creature.legendary-actions.description-aria"))
            )
          )
        )
      ),
      button(cls := "btn btn-success", `type` := "button", id := "add-legendary-action-button", onclick := "addLegendaryAction()")(
        messages("create-creature.legendary-actions.add")
      ),
      button(cls := "btn btn-success", `type` := "button", id := "remove-legendary-action-button", onclick := "removeLegendaryAction()")(
        messages("create-creature.legendary-actions.remove")
      )
    )
  }

  def apply(creatureForm: Form[CreatureDetail])(implicit messages: Messages): TypedTag[String] = {
    mainTemplate(pageTitle = messages("create-creature.title"), contentWidth = twoThirdsWidth)(
      h1(messages("create-creature.heading")),
      h2(messages("create-creature.subheading")),
      form(controllers.creatures.routes.CreateCreatureController.submit())(
        creatureNameDisplay(creatureForm),
        creatureDescriptionDisplay(creatureForm),
        div(cls := "row")(
          creatureSizeDisplay(creatureForm),
          creatureAlignmentDisplay(creatureForm),
          creatureArmourClassDisplay(creatureForm),
          creatureHitPointsDisplay(creatureForm),
          creatureChallengeRatingDisplay(creatureForm),
          creatureTypeDisplay(creatureForm)
        ),
        creatureTypeTagsDisplay(creatureForm),
        creatureMovementSpeedDisplay(creatureForm),
        creatureAbilityScoreDisplay(creatureForm),
        creatureSavingThrowDisplay(creatureForm),
        creatureSkillProficiencyDisplay(creatureForm),
        creatureResistanceDisplay(creatureForm),
        creatureConditionDisplay(creatureForm),
        creatureSenseDisplay(creatureForm),
        creatureLanguageDisplay(creatureForm),
        creatureTraitDisplay(creatureForm),
        creatureActionDisplay(creatureForm),
        creatureLegendaryActionDisplay(creatureForm),
        div(cls := "form-group")(
          button(cls := "btn btn-success", aria.label := messages("create-creature.continue.aria"))(messages("common.continue"))
        )
      )
    )
  }

}
