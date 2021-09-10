package topic

import java.util.UUID
import java.time.LocalDateTime

case class Topic(
    id: UUID,
    name: String,
    isDeleted: Boolean,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
)

case class CreateTopicCommand(name: String)

object Topic {
  def create(command: CreateTopicCommand): Topic =
    new Topic(
      UUID.randomUUID,
      command.name,
      false,
      LocalDateTime.now,
      LocalDateTime.now
    )
}
