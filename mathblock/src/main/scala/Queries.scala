package query

import java.util.UUID
import zio.ZIO
import getTopicResolver.GetTopicResolver.GetTopicResolverEnv
import topic.Topic

case class TopicArgs(id: String)

case class Queries(
    topic: TopicArgs => String
)
