package com.example.httpsjenkins

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._


object HttpsjenkinsRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" => Ok("Knock knock! who's there?")
        // for {
        //   joke <- J.get
        //   resp <- Ok(joke)
        // } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: GreetingService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "greet" / name =>
        for {
          greeting <- H.greet(GreetingService.Name(name))
          resp <- Ok(greeting)
        } yield resp

      case GET -> Root / "goodbye" => Ok("Goodbye")

    }
  }

}
