package config

import com.google.inject.AbstractModule
import connectors.{CampaignConnector, CampaignConnectorImpl}
import controllers._
import services.{CampaignService, CampaignServiceImpl}
import views.errors.{InternalServerError, InternalServerErrorImpl, NotFound, NotFoundImpl, OtherError, OtherErrorImpl}
import views.{CreateCampaign, CreateCampaignImpl, DeleteCampaign, DeleteCampaignImpl, EditCampaign, EditCampaignImpl, SelectCampaign, SelectCampaignImpl}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bindConfig()
    bindConnectors()
    bindControllers()
    bindServices()
    bindViews()
  }

  def bindConfig(): Unit = {
    bind(classOf[AppConfig]).to(classOf[AppConfigImpl]).asEagerSingleton()
  }

  def bindConnectors(): Unit = {
    bind(classOf[CampaignConnector]).to(classOf[CampaignConnectorImpl]).asEagerSingleton()
  }

  def bindControllers(): Unit = {
    bind(classOf[IndexController]).to(classOf[IndexControllerImpl]).asEagerSingleton()
    bind(classOf[SelectCampaignController]).to(classOf[SelectCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[CreateCampaignController]).to(classOf[CreateCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[EditCampaignController]).to(classOf[EditCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[DeleteCampaignController]).to(classOf[DeleteCampaignControllerImpl]).asEagerSingleton()
  }

  def bindServices(): Unit = {
    bind(classOf[CampaignService]).to(classOf[CampaignServiceImpl]).asEagerSingleton()
  }

  def bindViews(): Unit = {
    bind(classOf[SelectCampaign]).to(classOf[SelectCampaignImpl]).asEagerSingleton()
    bind(classOf[CreateCampaign]).to(classOf[CreateCampaignImpl]).asEagerSingleton()
    bind(classOf[EditCampaign]).to(classOf[EditCampaignImpl]).asEagerSingleton()
    bind(classOf[DeleteCampaign]).to(classOf[DeleteCampaignImpl]).asEagerSingleton()
    bind(classOf[InternalServerError]).to(classOf[InternalServerErrorImpl]).asEagerSingleton()
    bind(classOf[NotFound]).to(classOf[NotFoundImpl]).asEagerSingleton()
    bind(classOf[OtherError]).to(classOf[OtherErrorImpl]).asEagerSingleton()
  }

}
