import com.google.inject.AbstractModule
import controllers._

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[IndexController]).to(classOf[IndexControllerImpl]).asEagerSingleton()
    bind(classOf[HomeController]).to(classOf[HomeControllerImpl]).asEagerSingleton()
  }

}
