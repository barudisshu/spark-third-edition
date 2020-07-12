name := """spark-third-edition"""

maintainer := "Galudisu <galudisu@gmail.com>"

version := "0.1"

scalaVersion := "2.12.8"

lazy val sparkVersion = "2.4.5"
lazy val postgresqlVersino = "42.1.4"

// This work for jdk >= 8u131
javacOptions in Universal := Seq(
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:MaxRAMFraction=1",
  "-J-XshowSettings:vm"
)

// These options will be used for *all* versions.
scalacOptions := Seq(
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

resolvers ++= Seq(
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

fork in run := true
Compile / run / fork := true
scalafmtOnCompile := true
scalafmtTestOnCompile := true
scalafmtVersion := "1.2.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"      % sparkVersion,
  "org.apache.spark" %% "spark-sql"       % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-graphx"    % sparkVersion,
  "org.apache.spark" %% "spark-mllib"     % sparkVersion，
  "org.postgresql" %% "postgresql" % postgresqlVersion
)
