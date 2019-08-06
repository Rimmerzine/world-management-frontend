package forms

import helpers.UnitSpec
import play.api.data.{Form, FormError}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import utils.TestConstants

class PlaneFormSpec extends UnitSpec with TestConstants {

  trait Setup {
    val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

    val form: Form[(String, Option[String], String)] = PlaneForm.form
  }

  "bindFromRequest" must {
    "bind successfully" when {
      "optional fields are provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeDescription -> testPlaneDescription,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )

        form.bindFromRequest().value mustBe Some(testPlaneName, Some(testPlaneDescription), testPlaneAlignment)
      }
      "optional fields are not provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeAlignment -> testPlaneAlignment
        )

        form.bindFromRequest().value mustBe Some(testPlaneName, None, testPlaneAlignment)
      }
    }
    "have an error" when {
      "no plane name is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeAlignment -> testPlaneAlignment
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeName, PlaneForm.nameMissingError))
      }
      "plane name is longer than 50 characters" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> ("A" * 51),
          PlaneForm.planeAlignment -> testPlaneAlignment
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeName, PlaneForm.nameTooLongError))
      }
      "no alignment is provided" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeAlignment, PlaneForm.alignmentRequiredError))
      }
      "the alignment provided is not valid" in new Setup {
        implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = fakeRequest.withFormUrlEncodedBody(
          PlaneForm.planeName -> testPlaneName,
          PlaneForm.planeAlignment -> "invalid"
        )

        form.bindFromRequest().errors mustBe Seq(FormError(PlaneForm.planeAlignment, PlaneForm.alignmentRequiredError))
      }
    }
  }

}
