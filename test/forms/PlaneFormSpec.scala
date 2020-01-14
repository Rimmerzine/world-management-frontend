package forms

import helpers.UnitSpec
import play.api.data.{Form, FormError}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import testutil.TestConstants

class PlaneFormSpec extends UnitSpec with TestConstants {

  trait Setup {
    val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

    val form: Form[(String, Option[String], String)] = PlaneForm.form
  }

  "bindFromRequest" must {
    "bind successfully" when {
      "optional fields are provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> planeName,
          PlaneForm.planeDescription -> planeDescription,
          PlaneForm.planeAlignment -> planeAlignment
        )

        form.bindFromRequest().value mustBe Some(planeName, Some(planeDescription), planeAlignment)
      }
      "optional fields are not provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> planeName,
          PlaneForm.planeAlignment -> planeAlignment
        )

        form.bindFromRequest().value mustBe Some(planeName, None, planeAlignment)
      }
    }
    "have an error" when {
      "no plane name is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeAlignment -> planeAlignment
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeName, PlaneForm.nameMissingError.key))
      }
      "plane name is longer than 50 characters" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> ("A" * 51),
          PlaneForm.planeAlignment -> planeAlignment
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeName, PlaneForm.nameTooLongError.key))
      }
      "no alignment is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> planeName
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeAlignment, PlaneForm.alignmentRequiredError.key))
      }
      "the alignment provided is not valid" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> planeName,
          PlaneForm.planeAlignment -> "invalid"
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeAlignment, PlaneForm.alignmentRequiredError.key))
      }
    }
  }

}
