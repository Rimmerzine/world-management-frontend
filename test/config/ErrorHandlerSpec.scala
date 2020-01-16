package config

import helpers.UnitSpec
import org.mockito.Mockito.when
import play.api.i18n.DefaultMessagesApi
import play.api.test.FakeRequest
import play.api.test.Helpers._
import testutil.TestConstants
import views.errors.{InternalServerError, NotFound}
import org.mockito.ArgumentMatchers.any

class ErrorHandlerSpec extends UnitSpec with TestConstants {

  val internalServerError: InternalServerError = mock[InternalServerError]
  val notFound: NotFound = mock[NotFound]
  val defaultMessagesApi: DefaultMessagesApi = new DefaultMessagesApi()
  val appConfig: AppConfig = mock[AppConfig]

  val errorHandler: ErrorHandler = new ErrorHandler(internalServerError, notFound, defaultMessagesApi, appConfig)

  "onServerError" must {
    s"return $INTERNAL_SERVER_ERROR" in {
      when(internalServerError(any())) thenReturn emptyHtmlTag

      val result = errorHandler.onServerError(FakeRequest(), new Exception("testException"))
      status(result) mustBe INTERNAL_SERVER_ERROR
      contentType(result) mustBe Some("text/html")
    }
  }

  "onClientError" must {
    s"return $NOT_FOUND" when {
      s"the client error was a $NOT_FOUND" in {
        when(notFound(any())) thenReturn emptyHtmlTag

        val result = errorHandler.onClientError(FakeRequest(), NOT_FOUND, "not found")
        status(result) mustBe NOT_FOUND
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return $FORBIDDEN" when {
      s"the client error was a $FORBIDDEN" in {
        when(notFound(any())) thenReturn emptyHtmlTag

        val result = errorHandler.onClientError(FakeRequest(), FORBIDDEN, "forbidden")
        status(result) mustBe FORBIDDEN
        contentType(result) mustBe Some("text/html")
      }
    }
    s"return the status the error was" when {
      s"the client error was any other type of client error" in {
        when(internalServerError(any())) thenReturn emptyHtmlTag

        val result = errorHandler.onClientError(FakeRequest(), NOT_ACCEPTABLE, "not acceptable")
        status(result) mustBe NOT_ACCEPTABLE
        contentType(result) mustBe Some("text/html")
      }
    }
  }

}
