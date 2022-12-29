package sirs.stardrive.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.crypto.spec.SecretKeySpec

fun generateRSA(): RSAKey {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }
    
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKey = keyPair.public as RSAPublicKey
    val privateKey = keyPair.private as RSAPrivateKey

    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateOtpKey(): SecretKeySpec {
    val seed = ByteArray(128 / 8)
    SecureRandom().nextBytes(seed)
    return SecretKeySpec(seed, "AES")
}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {
    private lateinit var authTokenKey: RSAKey
    private lateinit var refreshTokenKey: RSAKey

    @Bean
    fun authManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: BCryptPasswordEncoder
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        }

        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun refreshTokenAuthenticationProvider(@Qualifier("refreshTokenDecoder") refreshTokenDecoder: JwtDecoder)
        = JwtAuthenticationProvider(refreshTokenDecoder)

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { }
        .authorizeHttpRequests { it.anyRequest().permitAll() }
        .oauth2ResourceServer { it.jwt() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .build()

    @Bean
    @Qualifier("accessToken")
    fun accessJwkSource(): JWKSource<SecurityContext> {
        authTokenKey = generateRSA()
        val jwkSet = JWKSet(authTokenKey)
        return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    @Bean
    @Qualifier("refreshToken")
    fun refreshJwkSource(): JWKSource<SecurityContext> {
        refreshTokenKey = generateRSA();
        val jwkSet = JWKSet(refreshTokenKey)
        return JWKSource { jwkSelector, context -> jwkSelector.select(jwkSet) }
    }

    @Bean
    @Primary
    @Qualifier("accessTokenDecoder")
    fun accessJwtDecoder(@Qualifier("accessToken") jwks: JWKSource<SecurityContext>): JwtDecoder =
        NimbusJwtDecoder.withPublicKey(authTokenKey.toRSAPublicKey()).build()

    @Bean
    @Qualifier("refreshTokenDecoder")
    fun refreshJwtDecoder(@Qualifier("refreshToken") jwks: JWKSource<SecurityContext>): JwtDecoder =
        NimbusJwtDecoder.withPublicKey(refreshTokenKey.toRSAPublicKey()).build()

    @Bean
    @Primary
    @Qualifier("accessTokenEncoder")
    fun accessJwtEncoder(@Qualifier("accessToken") jwks: JWKSource<SecurityContext>): JwtEncoder =
        NimbusJwtEncoder(jwks)

    @Bean
    @Qualifier("refreshTokenEncoder")
    fun refreshJwtEncoder(@Qualifier("refreshToken") jwks: JWKSource<SecurityContext>): JwtEncoder =
        NimbusJwtEncoder(jwks)

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun refreshTokenEncoder() = BCryptPasswordEncoder()

    @Bean
    fun totpSecretEncoder() = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource =
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", CorsConfiguration().apply {
                allowedOriginPatterns = listOf("*")
                allowedMethods = listOf("*")
                allowedHeaders = listOf("*")
                allowCredentials = true
            })
        }
}