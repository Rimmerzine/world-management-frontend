/* Add a type tag input field */
function addTypeTag() {
    var typeTagInputCount = $("#tag-group").children().length;
    $("#tag-group").children().last().clone()
        .find("input").attr("id", "type-tag_" + typeTagInputCount).attr("name", "type-tag[" + typeTagInputCount + "]").val("").end()
        .appendTo("#tag-group");
    $("#tag-group").children().last()
        .find("input").focus();
    $("#remove-type-tag-button").css("display", "inline-block");
}

/* Remove a type tag input field */
function removeTypeTag() {
    var typeTagInputs = $("#tag-group").children();
    typeTagInputs.last().remove();
    if(typeTagInputs.length <= 2) {
        $("#remove-type-tag-button").css("display", "none");
        $("#add-type-tag-button").focus();
    }
}

/* Add a language input field */
function addLanguage() {
    var languageInputCount = $("#language-group").children().length;
    $("#language-group").children().last().clone()
        .find("input").attr("id", "language_" + languageInputCount).attr("name", "language[" + languageInputCount + "]").val("").end()
        .appendTo("#language-group");
    $("#language-group").children().last()
        .find("input").focus();
    $("#remove-language-button").css("display", "inline-block");
}

/* Remove a language input field */
function removeLanguage() {
    var languageInputs = $("#language-group").children();
    languageInputs.last().remove()
    if(languageInputs.length <= 2) {
        $("#remove-language-button").css("display", "none");
        $("#add-language-button").focus();
    }
}

/* Add trait name and description input fields */
function addTrait() {
    var traitInputGroupsCount = $("#trait-group").children().length;
    $("#trait-group").children().last().clone()
        .find("input").attr("id", "trait-name_" + traitInputGroupsCount).attr("name", "trait-name[" + traitInputGroupsCount + "]").val("").end()
        .find("textarea").attr("id", "trait-description_" + traitInputGroupsCount).attr("name", "trait-description[" + traitInputGroupsCount + "]").val("").end()
        .appendTo("#trait-group");
    $("#trait-group").children().last()
        .find("input").focus();
    $("#remove-trait-button").css("display", "inline-block");
}

/* Remove trait name and description input fields */
function removeTrait() {
    var traitInputs = $("#trait-group").children();
    traitInputs.last().remove()
    if(traitInputs.length <= 2) {
        $("#remove-trait-button").css("display", "none")
        $("#add-trait-button").focus()
    }
}

/* Add action name and description input fields */
function addAction() {
    var actionInputGroupCount = $("#action-group").children().length;
    $("#action-group").children().last().clone()
        .find("input").attr("id", "action-name_" + actionInputGroupCount).attr("name", "action-name[" + actionInputGroupCount + "]").val("").end()
        .find("textarea").attr("id", "action-description_" + actionInputGroupCount).attr("name", "action-description[" + actionInputGroupCount + "]").val("").end()
        .appendTo("#action-group");
    $("#action-group").children().last()
        .find("input").focus();
    $("#remove-action-button").css("display", "inline-block");
}

/* Remove action name and description input fields */
function removeAction() {
    var actionInputs = $("#action-group").children();
    actionInputs.last().remove()
    if(actionInputs.length <= 2) {
        $("#remove-action-button").css("display", "none")
        $("#add-action-button").focus()
    }
}

/* Add legendary action name and description input fields */
function addLegendaryAction() {
    var legendaryActionInputGroupCount = $("#legendary-action-group").children().length;
    $("#legendary-action-group").children().last().clone()
        .find("input").attr("id", "legendary-action-name_" + legendaryActionInputGroupCount).attr("name", "legendary-action-name[" + legendaryActionInputGroupCount + "]").val("").end()
        .find("textarea").attr("id", "legendary-action-description_" + legendaryActionInputGroupCount).attr("name", "legendary-action-name[" + legendaryActionInputGroupCount + "]").val("").end()
        .appendTo("#legendary-action-group");
    $("#legendary-action-group").children().last()
        .find("input").focus();
    $("#remove-legendary-action-button").css("display", "inline-block");
}

/* Remove legendary action name and description input fields */
function removeLegendaryAction() {
    var legendaryActionInputs = $("#legendary-action-group").children();
    legendaryActionInputs.last().remove()
    if(legendaryActionInputs.length <= 2) {
        $("#remove-legendary-action-button").css("display", "none")
        $("#add-legendary-action-button").focus()
    }
}