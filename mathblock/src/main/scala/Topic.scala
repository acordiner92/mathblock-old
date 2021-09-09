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
