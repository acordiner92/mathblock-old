package topicRepository

import doobie.util.transactor.Transactor
import zio.Task
import topic.Topic
import doobie.postgres.*
import doobie.postgres.implicits.*
import doobie.implicits.*
import zio.interop.catz.*
import configuration.Configuration.ConfigurationEnv
import configuration.Configuration
import java.util.UUID
import doobie.util.query.Query0
import zio.*
import com.typesafe.config.Config

class TopicRepository(tnx: Transactor[Task]) extends TopicRepository.Service:
  import TopicRepository.*

  def get(id: UUID): Task[Option[Topic]] =
    SQL
      .get(id)
      .option
      .transact(tnx)

  def create(topic: Topic): Task[Topic] =
    SQL
      .create(topic)
      .run
      .transact(tnx)
      .foldM(err => Task.fail(err), _ => Task.succeed(topic))

  def update(topic: Topic): Task[Unit] =
    SQL
      .update(topic)
      .run
      .transact(tnx)
      .foldM(err => Task.fail(err), _ => Task.unit)

  def delete(id: UUID): Task[Boolean] =
    SQL.delete(id).run.transact(tnx).fold(_ => false, _ => true)

object TopicRepository {
  type TopicRepositoryEnv = Has[TopicRepository.Service]

  trait Service {
    def get(id: UUID): Task[Option[Topic]]
    def create(topic: Topic): Task[Topic]
    def update(topic: Topic): Task[Unit]
    def delete(id: UUID): Task[Boolean]
  }

  object SQL {
    def get(id: UUID) =
      sql"""SELECT * FROM topic WHERE ID = $id """.query[Topic]

    def create(topic: Topic) =
      sql"""INSERT INTO topic (id, name, is_deleted, created_at, updated_at) 
                VALUES (${topic.id}, ${topic.name}, ${topic.isDeleted}, ${topic.createdAt}, ${topic.updatedAt})""".update

    def update(topic: Topic) =
      sql"""UPDATE topic 
                SET name=${topic.name}, is_deleted = ${topic.isDeleted},  updated_at = ${topic.updatedAt})""".update

    def delete(id: UUID) =
      sql"""DELETE FROM topic WHERE id = $id""".update
  }

  val live: ZLayer[ConfigurationEnv, Throwable, TopicRepositoryEnv] =
    ZLayer.fromManaged {
      for {
        conf <- Configuration.load.toManaged_
      } yield new TopicRepository(
        Transactor.fromDriverManager[Task](
          "org.postgresql.Driver",
          conf.database.url,
          conf.database.username,
          conf.database.password
        )
      )
    }

}
