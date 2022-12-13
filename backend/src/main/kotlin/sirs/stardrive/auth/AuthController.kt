package sirs.stardrive.auth

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val tokenService: TokenService) {
    @PostMapping("/token")
    fun token(authentication: Authentication): String =
        tokenService.generateToken(authentication)
}