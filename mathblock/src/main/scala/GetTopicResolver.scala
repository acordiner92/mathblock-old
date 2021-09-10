package getTopicResolver

import topic.*
import zio.Task
import zio.Has
import zio.ZIO
import zio.ZLayer
import topicRepository.TopicRepository.TopicRepositoryEnv
import java.util.UUID

object GetTopicResolver {
  type GetTopicResolverEnv = Has[GetTopicResolver.Service]

  trait Service {
    def handle(id: UUID): Task[Option[Topic]]
  }

  val live: ZLayer[TopicRepositoryEnv, Nothing, GetTopicResolverEnv] =
    ZLayer.fromService(topicRepository =>
      new Service {
        override def handle(id: UUID) = topicRepository.get(id)
      }
    )

  def handle(
      id: UUID
  ): ZIO[GetTopicResolverEnv, Throwable, Option[Topic]] =
    ZIO.accessM(_.get.handle(id))
}
