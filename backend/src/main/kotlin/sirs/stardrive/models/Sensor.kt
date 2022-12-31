package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.security.PublicKey
import java.security.Timestamp

@Document
data class Sensor(
    @Indexed(unique = true) val name: String,
    var publicKey: String,
    var timestamp: Int = 0,
    var value: Int = 0,
    @MongoId val id: ObjectId? = null
) {
    constructor(newSensorDto: NewSensorDto) : this(newSensorDto.name, newSensorDto.key)
}

@Repository
interface SensorRepository : MongoRepository<Sensor, ObjectId> {
    fun findByName(name: String): Sensor?
}

data class NewSensorDto(val name: String, val key: String)

data class SensorDto(val name: String, val value: Int) {
    constructor(sensor: Sensor) : this(sensor.name, sensor.value)
}

data class UpdateSensorValueDto(val value: Int, val timestamp: Int, val signature: String)

data class ReadSensorValueDto(val value: Int)
