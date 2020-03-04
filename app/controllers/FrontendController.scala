package controllers

import controllers.utils.{SessionKeys, WritableTag}
import play.api.Logging
import play.api.mvc._

import scala.concurrent.Future

trait FrontendController extends MessagesBaseController with Logging with WritableTag {

  def withNavCollection(func: (String, List[String]) => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get(SessionKeys.journey) match {
      case Some(journey) if journey.nonEmpty =>
        val journeyHead :: tail = journey.split(',').toList
        func(journeyHead, journeyHead :: tail)
      case _ => Future.successful(Redirect(controllers.campaigns.routes.SelectCampaignController.show()))
    }
  }

}
