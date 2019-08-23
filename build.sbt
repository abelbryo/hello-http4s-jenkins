import com.typesafe.sbt.SbtNativePackager.{Docker, Universal}
import com.typesafe.sbt.packager.MappingsHelper._
import sbt._

val Http4sVersion   = "0.20.3"
val CirceVersion    = "0.11.1"
val Specs2Version   = "4.1.0"
val LogbackVersion  = "1.2.3"
val silencerVersion = "1.4.3"

lazy val pushImage =
  taskKey[Unit]("pushes docker image of project to remote registry")

def pushImageImpl = pushImage := {
  val log = streams.value.log
  log.info(s" staged @ ${(stage in Docker).value}")
  (publish in Docker).value
  val name = dockerAliases.value.flatMap(_.tag).mkString(" ")
  log.info(""" -------->>>>>>>>>> """ + name)
}

lazy val root = (project in file("."))
  .settings(
    scalafmtOnCompile := true,
    dockerRepository := Option("localhost"),
    dockerUsername := None,
    dockerExposedPorts := Seq(9000, 9001),
    dockerAlias := DockerAlias(
      (dockerRepository in Docker).value,
      (dockerUsername in Docker).value,
      (packageName in Docker).value,
      Option((version in Docker).value)
    ),
    pushImageImpl,
    organization := "com.example",
    name := "https-jenkins",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s"     %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"     %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"     %% "http4s-circe"        % Http4sVersion,
      "org.http4s"     %% "http4s-dsl"          % Http4sVersion,
      "io.circe"       %% "circe-generic"       % CirceVersion,
      "org.specs2"     %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic"      % LogbackVersion
    ) ++ Seq(
      compilerPlugin(
        "com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full
      ),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-P:silencer:globalFilters=.*deprecated:.*"
)
