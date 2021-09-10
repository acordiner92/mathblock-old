package query

import java.util.UUID
import zio.ZIO
import getTopicResolver.GetTopicResolver.GetTopicResolverEnv
import topic.Topic

case class Queries(
    topic: UUID => ZIO[GetTopicResolverEnv, Throwable, Option[Topic]]
)
