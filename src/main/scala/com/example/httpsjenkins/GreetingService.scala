package com.example.httpsjenkins

import cats.Applicative
import cats.implicits._
import io.circe.{ Encoder, Decoder, Json }
import io.circe.generic.semiauto._

import org.http4s.EntityEncoder
import org.http4s.circe._

trait GreetingService[F[_]] {
  def greet(n: GreetingService.Name): F[GreetingService.Greeting]
}

object GreetingService {
  implicit def apply[F[_]](implicit ev: GreetingService[F]): GreetingService[F] = ev

  final case class Name(name: String) extends AnyVal
  object Name {
    implicit def nameDecoder: Decoder[Name] = Decoder[Name] {
      _.as[String].map(Name(_))
    }
  }
  /**
   * More generally you will want to decouple your edge representations from
   * your internal data structures, however this shows how you can
   * create encoders for your data.
   */
  final case class Greeting(greeting: String, name: Name)

  object Greeting {

    implicit val greetingEncoder: Encoder[Greeting] = Encoder[Greeting] {
      a =>
        Json.obj(
          "message" -> Json.fromString(a.greeting),
          "to" -> Json.fromString(a.name.name))
    }

    implicit def greetingDecoder: Decoder[Greeting] = Decoder[Greeting] {
      c =>
        for {
          g <- c.downField("message").as[String]
          n <- c.downField("to").as[Name]
        } yield Greeting(g, n)
    }

    implicit def greetingEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Greeting] = jsonEncoderOf[F, Greeting]
  }

  def impl[F[_]: Applicative]: GreetingService[F] = new GreetingService[F] {

    def greet(n: GreetingService.Name): F[GreetingService.Greeting] =
      Greeting("Moi", n).pure[F]
  }
}
