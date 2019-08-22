package models

import helpers.UnitSpec
import play.api.libs.json._
import utils.TestConstants

class WorldElementSpec extends UnitSpec with TestConstants {

  "WorldElement" must {

    "replace" must {
      "return the element provided if the id matches the current world element" in {
        campaign.replace(campaign.id, campaignWithContent) mustBe campaignWithContent
      }
      "return the current world element with updated content if the element to replace exists within" in {
        campaignWithContent.replace(plane.id, land) mustBe campaignWithContent.copy(content = List(land))
      }
      "return the current world element unchanged if the element to replace doesn't exist in the element" in {
        campaignWithContent.replace(land.id, land) mustBe campaignWithContent
      }
    }

    "addElementTo" must {
      "return the current world element with a new content element if the id matches" in {
        campaign.addElementTo(campaign.id, plane) mustBe campaign.copy(content = List(plane))
      }
      "return the current world element with its content being changed if the element to add to is in the content" in {
        campaign.copy(content = List(plane)).addElementTo(plane.id, land) mustBe campaign.copy(content = List(plane.copy(content = List(land))))
      }
      "return the current world element unchanged if the element to add to doesn't exist in the content" in {
        campaign.copy(content = List(plane)).addElementTo(land.id, land) mustBe campaign.copy(content = List(plane))
      }
    }

    "removeElementId" must {
      "return the current world element with content removed if the id exists directly in its content" in {
        campaign.copy(content = List(plane)).removeElementId(plane.id) mustBe campaign
      }
      "return the current world element with sub-content removed if the id is found" in {
        campaign.copy(content = List(plane.copy(content = List(land)))).removeElementId(land.id) mustBe campaign.copy(content = List(plane))
      }
      "return the current world element unchanged if the element to remove doesn't exist in the element" in {
        campaign.copy(content = List(plane)).removeElementId(land.id) mustBe campaign.copy(content = List(plane))
      }
    }

    "find" must {
      "return the current world element if it is the searched element" in {
        campaign.find(campaign.id) mustBe Some(campaign)
      }
      "return the element within if the id is found" in {
        campaign.copy(content = List(plane)).find(plane.id) mustBe Some(plane)
      }
      "return None if the element id could not be found in the content" in {
        campaign.copy(content = List(plane)).find(land.id) mustBe None
      }
    }

    "read a campaign successfully" in {
      Json.fromJson[WorldElement](campaignJson) mustBe JsSuccess(campaign)
    }
    "write a campaign successfully" in {
      Json.toJson[WorldElement](campaign) mustBe campaignJson
    }

    "read a plane successfully" in {
      Json.fromJson[WorldElement](planeJson) mustBe JsSuccess(plane)
    }
    "write a plane successfully" in {
      Json.toJson[WorldElement](plane) mustBe planeJson
    }

    "read a land successfully" in {
      Json.fromJson[WorldElement](landJson) mustBe JsSuccess(land)
    }
    "write a land successfully" in {
      Json.toJson[WorldElement](land) mustBe landJson
    }

    "return an error when reading an invalid element" in {
      val json: JsObject = Json.obj(
        "elementType" -> "invalid",
        "id" -> "invalidElement",
        "name" -> "invalidElement",
        "description" -> "invalidElement",
        "content" -> Json.arr()
      )
      Json.fromJson[WorldElement](json) mustBe JsError(JsPath \ "elementType", "error.invalid")
    }

  }

  "Campaign" must {
    "updateContent" must {
      "replace content with the provided replacement" in {
        campaign.updateContent(replacement = List(campaign)) mustBe campaign.copy(content = List(campaign))
      }
    }

    "create" must {
      "return a created campaign" in {
        val created = Campaign.create(campaignName, Some(campaignDescription))
        created.elementType mustBe "campaign"
        created.name mustBe campaignName
        created.description mustBe Some(campaignDescription)
        created.content mustBe List.empty[WorldElement]
      }
    }
  }

  "Plane" must {
    "updateContent" must {
      "replace content with the provided replacement" in {
        plane.updateContent(replacement = List(plane)) mustBe plane.copy(content = List(plane))
      }
    }

    "create" must {
      "return a created plane" in {
        val created = Plane.create(planeName, Some(planeDescription), planeAlignment)
        created.elementType mustBe "plane"
        created.name mustBe planeName
        created.description mustBe Some(planeDescription)
        created.alignment mustBe planeAlignment
        created.content mustBe List.empty[WorldElement]
      }
    }
  }

  "Land" must {
    "updateContent" must {
      "replace content with the provided replacement" in {
        land.updateContent(replacement = List(land)) mustBe land.copy(content = List(land))
      }
    }

    "create" must {
      "return a created land" in {
        val created = Land.create(landName, Some(landDescription))
        created.elementType mustBe "land"
        created.name mustBe landName
        created.description mustBe Some(landDescription)
        created.content mustBe List.empty[WorldElement]
      }
    }
  }

}
