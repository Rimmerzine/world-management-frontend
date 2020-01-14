package connectors

import helpers.UnitSpec
import mockws.{MockWS, MockWSHelpers}
import models.Creature
import models.ErrorModel.{CreatureNotFound, JsonParseError, UnexpectedStatus}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import testutil.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreatureConnectorSpec extends UnitSpec with MockWSHelpers with TestConstants {

  val testBaseUrl: String = "testBaseUrl"

  class Setup(url: String, verb: String, result: Result) {

    val mockWS: MockWS = MockWS {
      case (`verb`, `url`) => Action.async {
        Future.successful(result)
      }
    }

    lazy val connector: CreatureConnector = new CreatureConnector {
      val wsClient: WSClient = mockWS
      val baseUrl: String = testBaseUrl
    }

  }

  "retrieveAllCreatures" must {
    "return a list of creatures" when {
      "Ok is returned with creature json" in new Setup(s"$testBaseUrl/creatures/retrieve", GET, Ok(Json.arr(creatureJson, creatureJson))) {
        await(connector.retrieveAllCreatures) mustBe Right(List(creature, creature))
      }
      "NoContent is returned" in new Setup(s"$testBaseUrl/creatures/retrieve", GET, NoContent) {
        await(connector.retrieveAllCreatures) mustBe Right(List.empty[Creature])
      }
    }
    "return a JsonParseError" when {
      "Ok is returned but could not be parsed into a list of creatures" in new Setup(s"$testBaseUrl/creatures/retrieve", GET, Ok(emptyJson)) {
        await(connector.retrieveAllCreatures) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/creatures/retrieve", GET, InternalServerError) {
        await(connector.retrieveAllCreatures) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "retrieveSingleCreature" must {
    "return a creature" when {
      "Ok is returned with a creature json" in new Setup(s"$testBaseUrl/creatures/retrieve/${creature.id}", GET, Ok(creatureJson)) {
        await(connector.retrieveSingleCreature(creature.id)) mustBe Right(creature)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a creature" in new Setup(s"$testBaseUrl/creatures/retrieve/${creature.id}", GET, Ok(emptyJson)) {
        await(connector.retrieveSingleCreature(creature.id)) mustBe Left(JsonParseError)
      }
    }
    "return CreatureNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/creatures/retrieve/${creature.id}", GET, NotFound) {
        await(connector.retrieveSingleCreature(creature.id)) mustBe Left(CreatureNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/creatures/retrieve/${creature.id}", GET, InternalServerError) {
        await(connector.retrieveSingleCreature(creature.id)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "createCreature" must {
    "return a creature" when {
      "Created is returned with a creature json" in new Setup(s"$testBaseUrl/creatures/create", POST, Created(creatureJson)) {
        await(connector.createCreature(creature)) mustBe Right(creature)
      }
    }
    "return JsonParseError" when {
      "Created is returned but could not be parsed into a creature" in new Setup(s"$testBaseUrl/creatures/create", POST, Created(emptyJson)) {
        await(connector.createCreature(creature)) mustBe Left(JsonParseError)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/creatures/create", POST, InternalServerError) {
        await(connector.createCreature(creature)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "updateCreature" must {
    "return a creature" when {
      "Ok is returned with a creature json" in new Setup(s"$testBaseUrl/creatures/update", PUT, Ok(creatureJson)) {
        await(connector.updateCreature(creature)) mustBe Right(creature)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a creature" in new Setup(s"$testBaseUrl/creatures/update", PUT, Ok(emptyJson)) {
        await(connector.updateCreature(creature)) mustBe Left(JsonParseError)
      }
    }
    "return CreatureNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/creatures/update", PUT, NotFound) {
        await(connector.updateCreature(creature)) mustBe Left(CreatureNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/creatures/update", PUT, InternalServerError) {
        await(connector.updateCreature(creature)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "removeCreature" must {
    "return a creature" when {
      "Ok is returned with a creature json" in new Setup(s"$testBaseUrl/creatures/remove/${creature.id}", DELETE, Ok(creatureJson)) {
        await(connector.removeCreature(creature.id)) mustBe Right(creature)
      }
    }
    "return JsonParseError" when {
      "Ok is returned but could not be parsed into a creature" in new Setup(s"$testBaseUrl/creatures/remove/${creature.id}", DELETE, Ok(emptyJson)) {
        await(connector.removeCreature(creature.id)) mustBe Left(JsonParseError)
      }
    }
    "return CreatureNotFound" when {
      "NotFound is returned" in new Setup(s"$testBaseUrl/creatures/remove/${creature.id}", DELETE, NotFound) {
        await(connector.removeCreature(creature.id)) mustBe Left(CreatureNotFound)
      }
    }
    "return UnexpectedStatus" when {
      "a status not matched is returned" in new Setup(s"$testBaseUrl/creatures/remove/${creature.id}", DELETE, InternalServerError) {
        await(connector.removeCreature(creature.id)) mustBe Left(UnexpectedStatus)
      }
    }
  }

}
