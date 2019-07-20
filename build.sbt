
name := "world-management-frontend"
 
version := "0.1"

lazy val scalaTest: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2"
lazy val jsoup: ModuleID = "org.jsoup" % "jsoup" % "1.11.3"
lazy val mockWS: ModuleID = "de.leanovate.play-mockws" %% "play-mockws" % "2.7.0"
lazy val scalaTags: ModuleID = "com.lihaoyi" %% "scalatags" % "0.7.0"

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;controllers\\..*Reverse*.*;router.Routes*.*;views\\..*",
    ScoverageKeys.coverageMinimum := 75,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}
      
lazy val `world-management-frontend` = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(scoverageSettings: _*)
  .settings(
    PlayKeys.playDefaultPort := 9901,
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      jdbc,
      ehcache,
      ws,
      specs2 % Test,
      guice,
      scalaTags,
      scalaTest % "it,test",
      jsoup % "it,test",
      mockWS % "it,test"

    )
  )
      
scalaVersion := "2.12.2"