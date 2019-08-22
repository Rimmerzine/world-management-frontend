package forms

import helpers.UnitSpec
import play.api.data.{Form, FormError}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import utils.TestConstants

class CampaignFormSpec extends UnitSpec with TestConstants {

  trait Setup {
    val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

    val form: Form[(String, Option[String])] = CampaignForm.form
  }

  "bindFromRequest" must {
    "bind successfully" when {
      "a valid campaign name and description is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          CampaignForm.campaignName -> campaignName,
          CampaignForm.campaignDescription -> campaignDescription
        )

        form.bindFromRequest().value mustBe Some(campaignName, Some(campaignDescription))
      }
      "a valid campaign name is provided with no description" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          CampaignForm.campaignName -> campaignName
        )

        form.bindFromRequest().value mustBe Some(campaignName, None)
      }
    }
    "have an error" when {
      "no campaign name is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody()

        form.bindFromRequest().errors mustBe Seq(FormError(CampaignForm.campaignName, CampaignForm.nameMissingError))
      }
      "campaign name is longer than 50 characters" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          CampaignForm.campaignName -> ("A" * 51)
        )

        form.bindFromRequest().errors mustBe Seq(FormError(CampaignForm.campaignName, CampaignForm.nameTooLongError))
      }
    }
  }

}
