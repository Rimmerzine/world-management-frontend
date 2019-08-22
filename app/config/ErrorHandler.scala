package config

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.http.Status._
import play.api.http.{ContentTypeOf, ContentTypes, HttpErrorHandler, Writeable}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc._
import scalatags.Text.Tag
import views.errors.{InternalServerError, NotFound, OtherError}

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(
                              val internalServerError: InternalServerError,
                              val notFound: NotFound,
                              val otherError: OtherError,
                              implicit val messagesApi: MessagesApi,
                              implicit val appConfig: AppConfig
                            ) extends HttpErrorHandler with Logging with I18nSupport {

  implicit def tagWritable(implicit codec: Codec): Writeable[Tag] = {
    Writeable(data => codec.encode("<!DOCTYPE html>" + data.render))
  }

  implicit def contentType(implicit codec: Codec): ContentTypeOf[Tag] = {
    ContentTypeOf(Some(ContentTypes.HTML))
  }

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful {
      statusCode match {
        case NOT_FOUND => Status(statusCode)(notFound())
        case FORBIDDEN => Status(statusCode)(notFound())
        case _ => Status(statusCode)(otherError())
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
