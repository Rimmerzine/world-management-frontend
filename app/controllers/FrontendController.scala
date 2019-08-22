package controllers

import play.api.Logging
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.i18n.I18nSupport
import play.api.mvc._
import scalatags.Text.Tag

import scala.concurrent.Future

trait FrontendController extends BaseController with I18nSupport with Logging with WritableTag {

  val journeyKey: String = "journey"

  def withNavCollection(func: (String, List[String]) => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get(journeyKey) match {
      case Some(journey) if journey.nonEmpty =>
        val journeyPath: List[String] = journey.split(',').toList
        func(journeyPath.head, journeyPath)
      case Some(_) | None => Future.successful(Redirect(controllers.campaigns.routes.SelectCampaignController.show()))
    }
  }

}

trait WritableTag {

  implicit def tagWritable(implicit codec: Codec): Writeable[Tag] = {
    Writeable(data => codec.encode("<!DOCTYPE html>" + data.render))
  }

  implicit def contentType(implicit codec: Codec): ContentTypeOf[Tag] = {
    ContentTypeOf(Some(ContentTypes.HTML))
  }

}
