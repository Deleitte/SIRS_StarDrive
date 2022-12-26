package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

const val DEFAULT_READ_INTERVAL = 5000

@Document
data class Sensor(
    @Indexed(unique = true) val name: String,
    var value: Int = 0,
    @MongoId val id: ObjectId? = null
) {
    constructor(newSensorDto: NewSensorDto) : this(newSensorDto.name, 0)
}

@Repository
interface SensorRepository : MongoRepository<Sensor, ObjectId> {
    fun findByName(name: String): Sensor?
}

data class NewSensorDto(val name: String) {
    constructor(sensor: Sensor) : this(sensor.name)
}

data class SensorDto(val name: String, val value: Int) {
    constructor(sensor: Sensor) : this(sensor.name, sensor.value)
}

data class UpdateSensorValueDto(val value: Int) {
    constructor(sensor: Sensor) : this(sensor.value)
}

data class ReadSensorValueDto(val value: Int) {
    constructor(sensor: Sensor) : this(sensor.value)
}