name := """spark-third-edition"""

maintainer := "Galudisu <galudisu@gmail.com>"

version := "0.1"

scalaVersion in ThisBuild := "2.12.8"
organization in ThisBuild := "info.galudisu"

lazy val global = project.in(file(".")).settings(settings).aggregate(part1)

lazy val part1 = project
  .in(file("part1"))
  .settings(
    name := "part1",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .aggregate(chap1)

lazy val chap1 = project
  .in(file("part1/chap1"))
  .settings(
    name := "chap1",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

// DEPENDENCIES

lazy val dependencies =
  new {
    val log4jV        = "2.7"
    val scalaLoggingV = "3.7.2"
    val slf4jV        = "1.7.25"

    val sparkV = "3.0.0"

    val log4jCore    = "org.apache.logging.log4j"   % "log4j-core"       % log4jV
    val log4jApi     = "org.apache.logging.log4j"   % "log4j-api"        % log4jV
    val log4jImpl    = "org.apache.logging.log4j"   % "log4j-slf4j-impl" % log4jV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingV
    val slf4j        = "org.slf4j"                  % "slf4j-api"        % slf4jV

    val sparkCore      = "org.apache.spark" %% "spark-core"      % sparkV
    val sparkSql       = "org.apache.spark" %% "spark-sql"       % sparkV
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkV
    val sparkGraphx    = "org.apache.spark" %% "spark-graphx"    % sparkV
    val sparkMllib     = "org.apache.spark" %% "spark-mllib"     % sparkV

  }

lazy val commonDependencies = Seq(
  dependencies.log4jCore,
  dependencies.log4jApi,
  dependencies.log4jImpl,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.sparkCore,
  dependencies.sparkSql,
  dependencies.sparkStreaming,
  dependencies.sparkGraphx,
  dependencies.sparkMllib
).map(_.exclude("org.slf4j", "slf4j-log4j12"))

// SETTINGS

lazy val settings =
  commonSettings ++
    scalafmtSettings

// These options will be used for *all* versions.
lazy val scalaCompilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

// This work for jdk >= 8u131
lazy val javacCompilerOptions = Seq(
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:MaxRAMFraction=1",
  "-J-XshowSettings:vm"
)

lazy val commonSettings = Seq(
  javacOptions in Universal ++= javacCompilerOptions,
  scalacOptions ++= scalaCompilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val scalafmtSettings =
  Seq(
    fork in run := true,
    Compile / run / fork := true,
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    scalafmtVersion := "1.2.0"
  )
