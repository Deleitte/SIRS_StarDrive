package sirs.stardrive.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.LogSigningConfig
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.Log
import sirs.stardrive.models.LogDto
import sirs.stardrive.models.LogRepository
import sirs.stardrive.models.UserRepository
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.LocalDateTime
import java.util.*


@Service
class LogService(private val logRepository: LogRepository, private val userRepository: UserRepository, private val logSigningConfig: LogSigningConfig) {
    fun createLog(message: String) {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt;
        val user = userRepository.findByUsername(jwt.subject) ?: throw StarDriveException(
            ErrorMessage.USER_NOT_FOUND,
            jwt.subject
        )
        val now = LocalDateTime.now()
        val logContent =  "{author: \"${user.username}\", message: \"$message\", timestamp: \"$now\"}";
        val keyBytes = Files.readAllBytes(Paths.get(logSigningConfig.privateKey))
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val privateKey: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)

        val signature: Signature = Signature.getInstance("SHA256WithRSA")
        signature.initSign(privateKey)
        signature.update(logContent.toByteArray())
        val signatureBytes: ByteArray = signature.sign()
        val signatureB64 = Base64.getEncoder().encodeToString(signatureBytes)

        logRepository.save(Log(user.username, message, signatureB64, now.toString()))
    }

    fun verifyLog(log: Log): Boolean {
        val keyBytes = Files.readAllBytes(Paths.get(logSigningConfig.publicKey))
        val keySpec = X509EncodedKeySpec(keyBytes)
        val publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)

        val signature: Signature = Signature.getInstance("SHA256WithRSA")
        signature.initVerify(publicKey)
        val logContent = "{author: \"${log.author}\", message: \"${log.message}\", timestamp: \"${log.timestamp}\"}"
        signature.update(logContent.toByteArray())
        return signature.verify(Base64.getDecoder().decode(log.signature))

    }

    fun getLogs(): List<LogDto> {
        return logRepository.findAll().map { LogDto(it, verifyLog(it)) }
    }
}