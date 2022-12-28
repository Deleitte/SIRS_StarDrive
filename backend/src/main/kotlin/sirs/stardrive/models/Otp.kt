package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document
data class Otp(
    @Indexed(unique = true)
    val username: String,
    val secret: String,
)

data class NewOtpDto(val username: String)

data class InitOtpDto(val secret: String) {
    constructor(otp: Otp) : this(otp.secret)
}

data class VerifyOtpDto(val username: String, val password: Int)

data class OtpTestDto(val result: Boolean)

@Repository
interface OtpRepository : MongoRepository<Otp, ObjectId> {
    fun findByUsername(username: String): Otp?
}