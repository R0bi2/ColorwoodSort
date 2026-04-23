val scala3Version = "3.8.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "colorwoodSort-SE(SS_2026)",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.2.4" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    // libraryDependencies += "org.scaclatic" %% "scalactic" % "3.2.10"

    // bonuspunkt
    coverageExcludedPackages := ".*TicTacToe.*"
  )
