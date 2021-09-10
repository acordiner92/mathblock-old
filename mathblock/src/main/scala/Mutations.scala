package mutation

import topic.*
import createTopicResolver.CreateTopicResolver.CreateTopicResolverEnv
import zio.ZIO

case class Mutations(
    createTopicCommand: CreateTopicCommand => ZIO[
      CreateTopicResolverEnv,
      Throwable,
      Topic
    ]
)
