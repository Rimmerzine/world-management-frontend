package config

import com.google.inject.AbstractModule
import connectors._
import controllers._
import controllers.campaigns._
import controllers.creatures._
import controllers.lands._
import controllers.planes._
import services._
import views._
import views.campaigns._
import views.creatures._
import views.errors._
import views.lands._
import views.planes._

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
    bind(classOf[CreatureConnector]).to(classOf[CreatureConnectorImpl]).asEagerSingleton()
    bind(classOf[CreateCreatureController]).to(classOf[CreateCreatureControllerImpl]).asEagerSingleton()
  }

  def bindControllers(): Unit = {
    bind(classOf[IndexController]).to(classOf[IndexControllerImpl]).asEagerSingleton()

    bind(classOf[SelectCampaignController]).to(classOf[SelectCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[CreateCampaignController]).to(classOf[CreateCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[EditCampaignController]).to(classOf[EditCampaignControllerImpl]).asEagerSingleton()
    bind(classOf[DeleteCampaignController]).to(classOf[DeleteCampaignControllerImpl]).asEagerSingleton()

    bind(classOf[CreatePlaneController]).to(classOf[CreatePlaneControllerImpl]).asEagerSingleton()
    bind(classOf[EditPlaneController]).to(classOf[EditPlaneControllerImpl]).asEagerSingleton()
    bind(classOf[DeletePlaneController]).to(classOf[DeletePlaneControllerImpl]).asEagerSingleton()

    bind(classOf[CreateLandController]).to(classOf[CreateLandControllerImpl]).asEagerSingleton()
    bind(classOf[EditLandController]).to(classOf[EditLandControllerImpl]).asEagerSingleton()
    bind(classOf[DeleteLandController]).to(classOf[DeleteLandControllerImpl]).asEagerSingleton()

    bind(classOf[SelectController]).to(classOf[SelectControllerImpl]).asEagerSingleton()

    bind(classOf[ToolsController]).to(classOf[ToolsControllerImpl]).asEagerSingleton()

    bind(classOf[SelectCreatureController]).to(classOf[SelectCreatureControllerImpl]).asEagerSingleton()
  }

  def bindServices(): Unit = {
    bind(classOf[CampaignService]).to(classOf[CampaignServiceImpl]).asEagerSingleton()
    bind(classOf[CreatureService]).to(classOf[CreatureServiceImpl]).asEagerSingleton()
  }

  def bindViews(): Unit = {
    bind(classOf[SelectCampaign]).to(classOf[SelectCampaignImpl]).asEagerSingleton()
    bind(classOf[CreateCampaign]).to(classOf[CreateCampaignImpl]).asEagerSingleton()
    bind(classOf[EditCampaign]).to(classOf[EditCampaignImpl]).asEagerSingleton()
    bind(classOf[DeleteCampaign]).to(classOf[DeleteCampaignImpl]).asEagerSingleton()

    bind(classOf[CreatePlane]).to(classOf[CreatePlaneImpl]).asEagerSingleton()
    bind(classOf[EditPlane]).to(classOf[EditPlaneImpl]).asEagerSingleton()
    bind(classOf[DeletePlane]).to(classOf[DeletePlaneImpl]).asEagerSingleton()

    bind(classOf[CreateLand]).to(classOf[CreateLandImpl]).asEagerSingleton()
    bind(classOf[EditLand]).to(classOf[EditLandImpl]).asEagerSingleton()
    bind(classOf[DeleteLand]).to(classOf[DeleteLandImpl]).asEagerSingleton()

    bind(classOf[InternalServerError]).to(classOf[InternalServerErrorImpl]).asEagerSingleton()
    bind(classOf[NotFound]).to(classOf[NotFoundImpl]).asEagerSingleton()

    bind(classOf[SelectElement]).to(classOf[SelectElementImpl]).asEagerSingleton()

    bind(classOf[Tools]).to(classOf[ToolsImpl]).asEagerSingleton()

    bind(classOf[SelectCreature]).to(classOf[SelectCreatureImpl]).asEagerSingleton()
    bind(classOf[CreateCreature]).to(classOf[CreateCreatureImpl]).asEagerSingleton()
  }

}
