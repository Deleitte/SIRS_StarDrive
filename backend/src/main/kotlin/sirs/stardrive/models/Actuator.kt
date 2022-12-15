package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

const val DEFAULT_PING_INTERVAL = 5000

@Document
data class Actuator(
    @Indexed(unique = true) val name: String,
    val pingInterval: Int = DEFAULT_PING_INTERVAL,
    val lastPing: LocalDateTime? = null,
    val id: ObjectId? = null
) {
    constructor(newActuatorDto: NewActuatorDto) : this(newActuatorDto.name, newActuatorDto.pingInterval)
}

@Repository
interface ActuatorRepository : MongoRepository<Actuator, ObjectId> {
    fun findByName(name: String): Actuator?
}

data class NewActuatorDto(val name: String, val pingInterval: Int = DEFAULT_PING_INTERVAL) {
    constructor(actuator: Actuator) : this(actuator.name, actuator.pingInterval)
}

data class ActuatorDto(val name: String, val pingInterval: Int, val lastPing: LocalDateTime? = null) {
    constructor(actuator: Actuator) : this(actuator.name, actuator.pingInterval, actuator.lastPing)
}
