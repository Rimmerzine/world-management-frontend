name := "world-management-frontend"
 
version := "0.1"

lazy val scalaTest: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2"
lazy val jsoup: ModuleID = "org.jsoup" % "jsoup" % "1.11.3"
      
lazy val `world-management-frontend` = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      jdbc,
      ehcache,
      ws,
      specs2 % Test,
      guice,
      scalaTest % "it,test",
      jsoup % "it,test"
    )
  )
      
scalaVersion := "2.12.2"