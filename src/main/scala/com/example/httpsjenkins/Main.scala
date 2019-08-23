package com.example.httpsjenkins

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    HttpsjenkinsServer.stream[IO].compile.drain.as(ExitCode.Success)
}
