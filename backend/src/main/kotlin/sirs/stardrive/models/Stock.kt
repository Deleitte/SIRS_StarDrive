package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document
data class Part(
    @Indexed(unique = true) val ref: Int,
    val name: String,
    var quantity: Int,
    val price: Int,
    @MongoId val id: ObjectId? = null) {
    constructor(newPartDto: NewPartDto) : this(newPartDto.ref, newPartDto.name, newPartDto.quantity, newPartDto.price)
}

data class PartDto(val ref: Int, val quantity: Int, val price: Int, val name: String) {
    constructor(part: Part) : this(part.ref, part.quantity, part.price, part.name)
}

data class NewPartDto(val ref: Int, val quantity: Int, val price: Int, val name: String)

data class UpdatePartQuantityDto(val quantity: Int, val price: Int, val name: String)

@Repository
interface PartRepository : MongoRepository<Part, ObjectId> {
    fun findByRef(ref: Int): Part?
}