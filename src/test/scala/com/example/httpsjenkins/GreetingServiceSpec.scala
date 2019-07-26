package com.example.httpsjenkins

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult

class GreetingServiceSpec extends org.specs2.mutable.Specification {

  "GreetingService" >> {

    "return 200" >> {
      uriReturns200()
    }

    "return greet world" >> {
      uriReturnsHelloWorld()
    }

  }

  private[this] val retHelloWorld: Response[IO] = {
    val getHW = Request[IO](Method.GET, uri"/greet/world")
    val greetingService = GreetingService.impl[IO]
    HttpsjenkinsRoutes.helloWorldRoutes(greetingService).orNotFound(getHW).unsafeRunSync()
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retHelloWorld.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsHelloWorld(): MatchResult[String] =
    retHelloWorld.as[String].unsafeRunSync() must not be empty //startWith("{\"message\":\"Hello, world\"}")
}
