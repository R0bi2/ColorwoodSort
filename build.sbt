val scala3Version = "3.8.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "colorwoodSort-SE(SS_2026)",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.2.4" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    libraryDependencies += ("org.scala-lang.modules" %% "scala-swing" % "3.0.0").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
    // libraryDependencies += "org.scaclatic" %% "scalactic" % "3.2.10"

    // bonuspunkt
    coverageExcludedPackages := ".*TicTacToe.*",

    // Docker-Deployment: sbt-assembly baut ein Fat-Jar mit allen Abhaengigkeiten
    assembly / mainClass := Some("de.htwg.se.colorwoodSort.colorwoodSort"),
    assembly / assemblyJarName := "colorwoodSort.jar",
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case PathList("META-INF", "versions", _, "module-info.class") => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )
  .enablePlugins(ScoverageSbtPlugin)
