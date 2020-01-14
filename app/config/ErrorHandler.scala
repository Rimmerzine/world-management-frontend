package config

import controllers.utils.WritableTag
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.http.HttpErrorHandler
import play.api.http.Status._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc._
import views.errors.{InternalServerError, NotFound}

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(val internalServerError: InternalServerError,
                             val notFound: NotFound,
                             implicit val messagesApi: MessagesApi,
                             implicit val appConfig: AppConfig) extends HttpErrorHandler with Logging with I18nSupport with WritableTag {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful {
      statusCode match {
        case NOT_FOUND => Status(statusCode)(notFound())
        case FORBIDDEN => Status(statusCode)(notFound())
        case _ => Status(statusCode)(internalServerError())
      }
    }
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    logger.error(s"[onServerError] Error occurred which was not recovered from. Url: ${request.uri}", exception)
    Future.successful(
      InternalServerError(internalServerError())
    )
  }
}
