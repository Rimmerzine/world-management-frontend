package services

import connectors.CampaignConnector
import helpers.UnitSpec
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import utils.TestConstants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CampaignServiceSpec extends UnitSpec with TestConstants {

  class Setup {
    val mockCampaignConnector: CampaignConnector = mock[CampaignConnector]

    val service: CampaignService = new CampaignService {
      val campaignConnector: CampaignConnector = mockCampaignConnector
    }
  }

  "retrieveAllCampaigns" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.retrieveAllCampaigns(any())) thenReturn Future.successful(Right(List(testCampaign, testCampaignMinimal)))

      await(service.retrieveAllCampaigns) mustBe Right(List(testCampaign, testCampaignMinimal))
    }
  }

  "retrieveSingleCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Right(testCampaign))

      await(service.retrieveSingleCampaign(testCampaign.id)) mustBe Right(testCampaign)
    }
  }

  "createCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.createCampaign(matches(testCampaign))(any())) thenReturn Future.successful(Right(testCampaign))

      await(service.createCampaign(testCampaign)) mustBe Right(testCampaign)
    }
  }

  "updateCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.updateCampaign(matches(testCampaign))(any())) thenReturn Future.successful(Right(testCampaign))

      await(service.updateCampaign(testCampaign)) mustBe Right(testCampaign)
    }
  }

  "removeCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.removeCampaign(matches(testCampaign.id))(any())) thenReturn Future.successful(Right(testCampaign))

      await(service.removeCampaign(testCampaign.id)) mustBe Right(testCampaign)
    }
  }

}
