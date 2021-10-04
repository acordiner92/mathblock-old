package environments

import configuration.Configuration
import topicRepository.TopicRepository
import getTopicResolver.GetTopicResolver
import createTopicResolver.CreateTopicResolver
import zio.{ZEnv}
import zio.console.Console

object Environments {
  val topicRepository = Configuration.live >>> TopicRepository.live
  val getTopicResolver = topicRepository >>> GetTopicResolver.live
  val createTopicResolver = topicRepository >>> CreateTopicResolver.live
  val appEnvironment = Console.live ++ getTopicResolver ++ createTopicResolver
}
