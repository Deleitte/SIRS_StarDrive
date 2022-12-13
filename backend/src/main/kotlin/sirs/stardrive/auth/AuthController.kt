package sirs.stardrive.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class LoginRequestDto(val username: String, val password: String)

data class LoginResponseDto(val token: String)

@RestController
class AuthController(private val tokenService: TokenService, private val authenticationManager: AuthenticationManager) {
    @PostMapping("/token")
    fun token(@RequestBody userLogin: LoginRequestDto): LoginResponseDto =
        LoginResponseDto(
            tokenService.generateToken(
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        userLogin.username,
                        userLogin.password
                    )
                )
            )
        )
}