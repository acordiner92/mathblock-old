package graphlqlServer

import getTopicResolver.GetTopicResolver.*
import createTopicResolver.CreateTopicResolver.*
import zio.clock.Clock
import zio.console.Console
import zio.duration.*
import caliban.wrappers.Wrappers.*
import caliban.GraphQL
import caliban.GraphQL.graphQL
import caliban.RootResolver
import caliban.schema.GenericSchema
import caliban.wrappers.ApolloTracing.apolloTracing
import caliban.wrappers.Wrappers.*
import getTopicResolver.GetTopicResolver
import createTopicResolver.CreateTopicResolver
import topic.*
import mutation.Mutations
import query.Queries

import scala.language.postfixOps
import java.util.UUID

object GraphqlServer
    extends GenericSchema[GetTopicResolverEnv & CreateTopicResolverEnv] {

  val api: GraphQL[
    Console & Clock & GetTopicResolverEnv & CreateTopicResolverEnv
  ] = {
    graphQL(
      RootResolver(
        Queries(GetTopicResolver.handle(_)),
        Mutations(CreateTopicResolver.handle(_))
      )
    ) @@ maxFields(200) @@
      maxDepth(30) @@
      timeout(3 seconds) @@
      printSlowQueries(500 millis) @@
      printErrors @@
      apolloTracing
  }
}
