package sirs.stardrive.controllers

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.models.LoginRequestDto
import sirs.stardrive.models.LoginResponseDto
import sirs.stardrive.models.NewUserDto
import sirs.stardrive.services.TokenService
import sirs.stardrive.services.UserService

@RestController
class AuthController(
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService
) {
    @PostMapping("/token")
    fun token(@RequestBody userLogin: LoginRequestDto): LoginResponseDto {
        val authenticationToken = UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val token = tokenService.generateToken(authentication)
        return LoginResponseDto(token)
    }

    @PostMapping("/register")
    fun register(@RequestBody newUserDto: NewUserDto): LoginResponseDto {
        userService.createUser(newUserDto)
        val authenticationToken = UsernamePasswordAuthenticationToken(newUserDto.username, newUserDto.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val token = tokenService.generateToken(authentication)
        return LoginResponseDto(token)
    }
}