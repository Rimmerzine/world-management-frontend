package services

import connectors.CampaignConnector
import helpers.UnitSpec
import models.{Campaign, WorldElement}
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import utils.ErrorModel.{CampaignNotFound, ElementNotFound, UnexpectedStatus}
import utils.{ErrorModel, TestConstants}

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
      when(mockCampaignConnector.retrieveAllCampaigns(any())) thenReturn Future.successful(Right(List(campaign, campaign)))

      await(service.retrieveAllCampaigns) mustBe Right(List(campaign, campaign))
    }
  }

  "retrieveCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))

      await(service.retrieveCampaign(campaign.id)) mustBe Right(campaign)
    }
  }

  "createCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.createCampaign(matches(campaign))(any())) thenReturn Future.successful(Right(campaign))

      await(service.createCampaign(campaign)) mustBe Right(campaign)
    }
  }

  "updateCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.updateCampaign(matches(campaign))(any())) thenReturn Future.successful(Right(campaign))

      await(service.updateCampaign(campaign)) mustBe Right(campaign)
    }
  }

  "removeCampaign" must {
    "return back what it receives from the connector" in new Setup {
      when(mockCampaignConnector.removeCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))

      await(service.removeCampaign(campaign.id)) mustBe Right(campaign)
    }
  }

  "retrieveElement" must {
    "return back an element from the specified campaign with the specified element id" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Right(campaignWithContent))

      await(service.retrieveElement(campaignWithContent.id, plane.id)) mustBe Right(plane)
    }
    "return back ElementNotFound if the element is not in the campaign" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))

      await(service.retrieveElement(campaign.id, plane.id)) mustBe Left(ElementNotFound)
    }
    "return back the left returned from the connector" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))

      await(service.retrieveElement(campaignWithContent.id, plane.id)) mustBe Left(CampaignNotFound)
    }
  }

  "addElement" must {
    "update the selected campaign, adding an element to another element" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
      when(mockCampaignConnector.updateCampaign(matches(campaignWithContent))(any())) thenReturn Future.successful(Right(campaignWithContent))

      await(service.addElement(campaign.id, campaign.id, plane)) mustBe Right(campaignWithContent)
    }

    "return back an error" when {
      "finding the campaign returned an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Left(CampaignNotFound))

        await(service.addElement(campaign.id, campaign.id, plane)) mustBe Left(CampaignNotFound)
      }
      "updating the campaign returned an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaign.id))(any())) thenReturn Future.successful(Right(campaign))
        when(mockCampaignConnector.updateCampaign(matches(campaignWithContent))(any())) thenReturn Future.successful(Left(CampaignNotFound))

        await(service.addElement(campaign.id, campaign.id, plane)) mustBe Left(CampaignNotFound)
      }
    }
  }

  "removeElement" must {
    "update the campaign with the chosen element removed" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Right(campaignWithContent))
      when(mockCampaignConnector.updateCampaign(matches(campaign))(any())) thenReturn Future.successful(Right(campaign))

      await(service.removeElement(campaign.id, plane.id)) mustBe Right(campaign)
    }
    "return an error from the connector" when {
      "retrieving the campaign returns an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        await(service.removeElement(campaign.id, plane.id)) mustBe Left(UnexpectedStatus)
      }
      "updating the campaign returns an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Right(campaignWithContent))
        when(mockCampaignConnector.updateCampaign(matches(campaign))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        await(service.removeElement(campaign.id, plane.id)) mustBe Left(UnexpectedStatus)
      }
    }
  }

  "replaceElement" must {
    "update the campaign having replaced the specified element" in new Setup {
      when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Right(campaignWithContent))
      when(mockCampaignConnector.updateCampaign(matches(campaignWithContent.copy(content = List(plane.copy(name = "updatedPlaneName")))))(any()))
        .thenReturn(Future.successful(Right(campaignWithContent.copy(content = List(plane.copy(name = "updatedPlaneName"))))))

      val result: Either[ErrorModel, WorldElement] = await(service.replaceElement(campaignWithContent.id, plane.copy(name = "updatedPlaneName")))
      val expected: Campaign = campaignWithContent.copy(content = List(plane.copy(name = "updatedPlaneName")))

      result mustBe Right(expected)
    }

    "return an error from the connector" when {
      "retrieving the campaign returns an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Left(UnexpectedStatus))

        await(service.replaceElement(campaignWithContent.id, plane)) mustBe Left(UnexpectedStatus)
      }
      "updating the campaign returns an error" in new Setup {
        when(mockCampaignConnector.retrieveSingleCampaign(matches(campaignWithContent.id))(any())) thenReturn Future.successful(Right(campaignWithContent))
        when(mockCampaignConnector.updateCampaign(matches(campaignWithContent.copy(content = List(plane.copy(name = "updatedPlaneName")))))(any()))
          .thenReturn(Future.successful(Left(UnexpectedStatus)))

        await(service.replaceElement(campaignWithContent.id, plane.copy(name = "updatedPlaneName"))) mustBe Left(UnexpectedStatus)
      }
    }
  }

}
