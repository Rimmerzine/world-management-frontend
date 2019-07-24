package config

import javax.inject.Inject
import play.api.Configuration

class AppConfigImpl @Inject()(configuration: Configuration) extends AppConfig {

  val githubLink: String = configuration.get[String]("github.url")

  val backendUrl: String = configuration.get[String]("world-management.url")

}

trait AppConfig {

  def githubLink: String

  def backendUrl: String

  val homeLink: String = controllers.campaigns.routes.SelectCampaignController.show().url

}
