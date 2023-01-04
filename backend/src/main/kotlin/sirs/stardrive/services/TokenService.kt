package sirs.stardrive.services

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class TokenService(
    @Qualifier("accessTokenEncoder") private val accessTokenEncoder: JwtEncoder,
    @Qualifier("refreshTokenEncoder") private val refreshTokenEncoder: JwtEncoder,
    private val userService: UserService
) {

    private data class Claim(val claimName: String, val claimValue: Any)

    private data class Duration(val amount: Long, val unit: ChronoUnit)

    private fun generateToken(
        authentication: Authentication,
        encoder: JwtEncoder,
        duration: Duration,
        tokenClaims: List<Claim>
    ): Jwt {
        val now: Instant = Instant.now()
        val claims = JwtClaimsSet.builder()
            .id(UUID.randomUUID().toString())
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(duration.amount, duration.unit))
            .subject(authentication.name)
        tokenClaims.forEach { claims.claim(it.claimName, it.claimValue) }
        return encoder.encode(JwtEncoderParameters.from(claims.build()))
    }

    fun generateAccessToken(authentication: Authentication): String {
        val user = userService.loadUserByUsername(authentication.name)
        val authorities = user.authorities
        return generateToken(
            authentication,
            accessTokenEncoder,
            Duration(5, ChronoUnit.MINUTES), // TODO: duration in application.properties
            listOf(
                Claim("scope", authorities.joinToString(" ") as Any),
                Claim("2FA", false as Any)
            )
        ).tokenValue
    }

    @Throws(StarDriveException::class)
    fun renewAccessToken(authentication: Authentication): String {
        val username = authentication.name
        val refreshToken = authentication.principal as Jwt
        if (!userService.validateRefreshToken(username, refreshToken))
            throw StarDriveException(ErrorMessage.INVALID_REFRESH_TOKEN)
        return generateAccessToken(authentication)
    }

    @Throws(StarDriveException::class)
    fun generateRefreshToken(authentication: Authentication): String {
        val username = authentication.name
        val refreshToken = generateToken(
            authentication,
            refreshTokenEncoder,
            Duration(1, ChronoUnit.DAYS), // TODO: duration in application.properties
            emptyList()
        )

        userService.updateRefreshToken(username, refreshToken)
        return refreshToken.tokenValue
    }

    fun revokeRefreshToken(authentication: Authentication) {
        userService.revokeRefreshToken(authentication.name)
    }

    @Throws(StarDriveException::class)
    fun generate2FaToken(authentication: Authentication, guess: Int): String {
        val user = userService.loadUserByUsername(authentication.name)
        if (userService.validateTotp(authentication.name, guess))
            return generateToken(
                authentication,
                accessTokenEncoder,
                Duration(5, ChronoUnit.MINUTES),
                listOf(
                    Claim("scope", user.authorities.joinToString(" ") as Any),
                    Claim("2FA", true as Any)
                )
            ).tokenValue
        throw StarDriveException(ErrorMessage.OTP_ALREADY_EXISTS)
    }

}
