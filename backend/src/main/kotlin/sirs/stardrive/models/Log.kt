package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Document
data class Log(
    val author: String,
    val message: String,
    val signature: String,
    val timestamp: String,
    @MongoId val id: ObjectId? = null
)

@Repository
interface LogRepository : MongoRepository<Log, ObjectId> {
    fun findByAuthor(author: String): List<Log>
}

data class LogDto(
    val author: String,
    val message: String,
    val timestamp: String,
    val verified: Boolean,
) {
    constructor(log: Log, verified: Boolean) : this(log.author, log.message, log.timestamp, verified)
}
