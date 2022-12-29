package sirs.stardrive.services

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import java.time.Instant
import java.time.temporal.ChronoUnit

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
    ): String {
        val now: Instant = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(duration.amount, duration.unit))
            .subject(authentication.name)
        tokenClaims.forEach { claims.claim(it.claimName, it.claimValue) }

        return encoder.encode(JwtEncoderParameters.from(claims.build())).tokenValue
    }

    fun generateAccessToken(authentication: Authentication) = generateToken(
        authentication,
        accessTokenEncoder,
        Duration(5, ChronoUnit.MINUTES),
        listOf(
            Claim("scope", authentication.authorities.joinToString(" ") as Any),
            Claim("2FA", false as Any)
        )
    )

    @Throws(StarDriveException::class)
    fun renewAccessToken(authentication: Authentication, refreshToken: String): String {
        val username = authentication.name
        if (!userService.validateRefreshToken(username, refreshToken))
            throw StarDriveException(ErrorMessage.INVALID_REFRESH_TOKEN)
        return generateAccessToken(authentication)
    }

    @Throws(StarDriveException::class)
    fun generateRefreshToken(authentication: Authentication): String {
        val username = authentication.name
        // TODO: I think we should automatically revoke refresh tokens on new login attempts
        /*if (userService.hasRefreshToken(username))
            throw StarDriveException(ErrorMessage.VALID_REFRESH_TOKEN)*/

        val refreshToken = generateToken(
            authentication,
            refreshTokenEncoder,
            Duration(1, ChronoUnit.DAYS),
            emptyList()
        )
        return userService.updateRefreshToken(username, refreshToken)
    }

    fun revokeRefreshToken(authentication: Authentication) {
        userService.revokeRefreshToken(authentication.name)
    }

    fun generate2FaToken(authentication: Authentication) = generateToken(
        authentication,
        accessTokenEncoder,
        Duration(5, ChronoUnit.MINUTES),
        listOf(
            Claim("scope", authentication.authorities.joinToString(" ") as Any),
            Claim("2FA", true as Any)
        )
    )
}
