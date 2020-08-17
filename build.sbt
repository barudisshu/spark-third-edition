name := """spark-third-edition"""

maintainer := "Galudisu <galudisu@gmail.com>"

version := "0.1"

scalaVersion in ThisBuild := "2.12.12"
organization in ThisBuild := "info.galudisu"

lazy val global = project.in(file(".")).settings(settings).aggregate(part1, part2)

lazy val part1 = project
  .in(file("part1"))
  .settings(
    name := "part1",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .aggregate(chap1, chap2, chap3, chap4, chap5, chap6)

lazy val part2 = project
  .in(file("part2"))
  .settings(
    name := "part2",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .aggregate(chap7)

lazy val chap1 = project
  .in(file("part1/chap1"))
  .settings(
    name := "chap1",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap2 = project
  .in(file("part1/chap2"))
  .settings(
    name := "chap2",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap3 = project
  .in(file("part1/chap3"))
  .settings(
    name := "chap3",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap4 = project
  .in(file("part1/chap4"))
  .settings(
    name := "chap4",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap5 = project
  .in(file("part1/chap5"))
  .settings(
    name := "chap5",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap6 = project
  .in(file("part1/chap6"))
  .settings(
    name := "chap6",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

lazy val chap7 = project
  .in(file("part2/chap7"))
  .settings(
    name := "chap7",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)

// DEPENDENCIES

lazy val dependencies =
  new {
    val log4jV        = "2.13.3"
    val scalaLoggingV = "3.7.2"
    val slf4jV        = "1.7.25"

    lazy val mysqlDriverV = "8.0.13"

    val sparkV    = "3.0.0"
    val sparkXmlV = "0.9.0"

    val log4jCore    = "org.apache.logging.log4j"   % "log4j-core"       % log4jV
    val log4jApi     = "org.apache.logging.log4j"   % "log4j-api"        % log4jV
    val log4jImpl    = "org.apache.logging.log4j"   % "log4j-slf4j-impl" % log4jV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingV
    val slf4j        = "org.slf4j"                  % "slf4j-api"        % slf4jV

    val mysql = "mysql" % "mysql-connector-java" % mysqlDriverV

    val sparkCore      = "org.apache.spark" %% "spark-core"      % sparkV
    val sparkSql       = "org.apache.spark" %% "spark-sql"       % sparkV
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkV
    val sparkGraphx    = "org.apache.spark" %% "spark-graphx"    % sparkV
    val sparkMllib     = "org.apache.spark" %% "spark-mllib"     % sparkV

    val sparkAvro = "org.apache.spark" %% "spark-avro" % sparkV
    val sparkXml = "com.databricks" %% "spark-xml" % sparkXmlV

  }

lazy val commonDependencies = Seq(
  dependencies.log4jCore,
  dependencies.log4jApi,
  dependencies.log4jImpl,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.mysql,
  dependencies.sparkCore,
  dependencies.sparkSql,
  dependencies.sparkStreaming,
  dependencies.sparkGraphx,
  dependencies.sparkMllib,
  dependencies.sparkAvro,
  dependencies.sparkXml,
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
