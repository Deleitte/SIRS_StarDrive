package sirs.stardrive.controllers

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.models.LoginRequestDto
import sirs.stardrive.models.LoginResponseDto
import sirs.stardrive.services.TokenService

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