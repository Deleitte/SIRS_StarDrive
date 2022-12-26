package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

const val DEFAULT_PING_INTERVAL = 5000

@Document
data class Actuator(
    @Indexed(unique = true) val name: String,
    var pingInterval: Int = DEFAULT_PING_INTERVAL,
    var on : Boolean = false,
    var damaged : Boolean = false,
    @MongoId val id: ObjectId? = null
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

data class ActuatorDto(val name: String, val pingInterval: Int, val on: Boolean, val damaged: Boolean) {
    constructor(actuator: Actuator) : this(actuator.name, actuator.pingInterval, actuator.on, actuator.damaged)
}

data class UpdatePingIntervalDto(val pingInterval: Int) {
    constructor(actuator: Actuator) : this(actuator.pingInterval)
}

data class UpdateActuatorStatusDto(val damaged: Boolean) {
    constructor(actuator: Actuator) : this(actuator.damaged)
}