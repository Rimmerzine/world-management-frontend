package forms

import helpers.UnitSpec
import play.api.data.{Form, FormError}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import utils.TestConstants

class LandFormSpec extends UnitSpec with TestConstants {

  trait Setup {
    val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

    val form: Form[(String, Option[String])] = LandForm.form
  }

  "bindFromRequest" must {
    "bind successfully" when {
      "optional fields are provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          LandForm.landName -> testLandName,
          LandForm.landDescription -> testLandDescription
        )

        form.bindFromRequest().value mustBe Some(testLandName, Some(testLandDescription))
      }
      "optional fields are not provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          LandForm.landName -> testLandName
        )

        form.bindFromRequest().value mustBe Some(testLandName, None)
      }
    }
    "have an error" when {
      "no land name is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody()

        form.bindFromRequest().errors mustBe Seq(FormError(LandForm.landName, LandForm.nameMissingError))
      }
      "land name is longer than 50 characters" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          LandForm.landName -> ("A" * 51)
        )

        form.bindFromRequest().errors mustBe Seq(FormError(LandForm.landName, LandForm.nameTooLongError))
      }
    }
  }

}
