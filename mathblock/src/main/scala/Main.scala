import zio.{ZIO, ZEnv, ExitCode, App}
import zhttp.http.*
import zio.console.{putStrLn}
import zhttp.service.Server
import zio.stream.ZStream
import scala.util.Try
import zhttp.service.server.ServerChannelFactory
import zhttp.service.EventLoopGroup
import pureconfig.ConfigSource
import configuration.*
import pureconfig.*
import pureconfig.generic.derivation.default.*
import caliban.ZHttpAdapter
import graphlqlServer.GraphqlServer
import createTopicResolver.CreateTopicResolver
import getTopicResolver.GetTopicResolver
import topicRepository.TopicRepository

object Mathblock extends App {
  private val PORT = 8080

  private val graphiql = Http.succeed(
    Response.http(content =
      HttpData.fromStream(ZStream.fromResource("graphiql.html"))
    )
  )

  private val server =
    GraphqlServer.api.interpreter.map(interpreter =>
      Server.port(PORT) ++
        Server.app(
          Http.route {
            case _ -> Root / "api" / "graphql" =>
              ZHttpAdapter.makeHttpService(interpreter)
            case _ -> Root / "graphiql" => graphiql
          }
        )
    )

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    val nThreads: Int =
      args.headOption.flatMap(x => Try(x.toInt).toOption).getOrElse(0)

    server
      .flatMap(x =>
        x.make
          .use(_ => putStrLn(s"Server started on port $PORT") *> ZIO.never)
          .provideCustomLayer(
            ServerChannelFactory.auto ++ EventLoopGroup
              .auto(
                nThreads
              ) ++ ZEnv.live ++ Configuration.live ++ TopicRepository.live ++ GetTopicResolver.live
          )
      )
      .exitCode

}
