package configuration

import zio.Task
import zio.ZIO
import zio.Has
import zio.ZLayer
import pureconfig.*
import pureconfig.generic.derivation.default.*

case class EnvironmentConfiguration(
    api: ApiConfiguration,
    database: DatabaseConfiguration
) derives ConfigReader
case class ApiConfiguration(port: Int)

case class DatabaseConfiguration(
    url: String,
    username: String,
    password: String
)

object Configuration {
  type ConfigurationEnv = Has[Configuration.Service]

  trait Service {
    def load: Task[EnvironmentConfiguration]
  }

  val live: ZLayer[Any, Nothing, ConfigurationEnv] =
    ZLayer.succeed(new Service {
      override def load =
        Task.effect(ConfigSource.default.loadOrThrow[EnvironmentConfiguration])
    })

  def load: ZIO[ConfigurationEnv, Throwable, EnvironmentConfiguration] =
    ZIO.accessM(_.get.load)
}
