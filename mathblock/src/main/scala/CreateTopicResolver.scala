package createTopicResolver

import topic.*
import zio.Task
import zio.Has
import zio.ZIO
import zio.ZLayer
import topicRepository.TopicRepository.TopicRepositoryEnv

object CreateTopicResolver {
  type CreateTopicResolverEnv = Has[CreateTopicResolver.Service]

  trait Service {
    def handle(command: CreateTopicCommand): Task[Topic]
  }

  val live: ZLayer[TopicRepositoryEnv, Nothing, CreateTopicResolverEnv] =
    ZLayer.fromService(topicRepository =>
      new Service {
        override def handle(command: CreateTopicCommand) =
          val topic = Topic.create(command)
          topicRepository.create(topic)
      }
    )

  def handle(
      command: CreateTopicCommand
  ): ZIO[CreateTopicResolverEnv, Throwable, Topic] =
    ZIO.accessM(_.get.handle(command))
}
