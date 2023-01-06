package sirs.stardrive.controllers

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.models.*
import sirs.stardrive.services.TokenService
import sirs.stardrive.services.UserService

@RestController
class AuthController(
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager,
    private val refreshTokenAuthProvider: JwtAuthenticationProvider,
    private val userService: UserService
) {

    // 1 hour
    private val cookieMaxAge = 3600

    @PostMapping("/auth/token")
    fun token(response: HttpServletResponse, @RequestBody userLogin: LoginRequestDto): LoginResponseDto {
        val authenticationToken = UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val token = tokenService.generateAccessToken(authentication)
        val refreshTokenCookie = Cookie("refresh-token", tokenService.generateRefreshToken(authentication)).apply {
            maxAge = cookieMaxAge
            secure = true
            isHttpOnly = true
            path = "/"
        }

        response.addCookie(refreshTokenCookie)
        return LoginResponseDto(token)
    }

    @PostMapping("/auth/token/refresh")
    fun refreshToken(
        request: HttpServletRequest,
        @CookieValue(name = "refresh-token") refreshToken: String
    ): LoginResponseDto {
        // TODO isto está partido quando se faz refresh e a revogar os tokens antigos
        val authentication = refreshTokenAuthProvider.authenticate(BearerTokenAuthenticationToken(refreshToken))
        return LoginResponseDto(tokenService.renewAccessToken(authentication))
    }

    @PostMapping("/auth/token/revoke")
    @PreAuthorize("isAuthenticated()")
    fun revokeToken(response: HttpServletResponse) {
        val authentication = SecurityContextHolder.getContext().authentication!!
        tokenService.revokeRefreshToken(authentication)

        val deleteRefreshTokenCookie = Cookie("refresh-token", null).apply {
            maxAge = 0
        }
        response.addCookie(deleteRefreshTokenCookie)
    }

    @PostMapping("/auth/register")
    fun register(@RequestBody newUserDto: NewUserDto): RegisterResponseDto {
        userService.createUser(newUserDto)
        val authenticationToken = UsernamePasswordAuthenticationToken(newUserDto.username, newUserDto.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val token = tokenService.generateAccessToken(authentication)
        return RegisterResponseDto(token, userService.getTotpSecret(authentication.name))
    }

    @PostMapping("/auth/token/2fa")
    @PreAuthorize("isAuthenticated()")
    fun totp(response: HttpServletResponse, @RequestBody totpDto: TotpDto): LoginResponseDto {
        val authentication = SecurityContextHolder.getContext().authentication!!
        val token = tokenService.generate2FaToken(authentication, totpDto.guess)
        val refreshTokenCookie = Cookie("refresh-token", tokenService.generateRefreshToken(authentication)).apply {
            maxAge = cookieMaxAge
            secure = true
            isHttpOnly = true
            path = "/"
        }
        response.addCookie(refreshTokenCookie)
        return LoginResponseDto(token)
    }

    @PostMapping("/auth/changepassword")
    @PreAuthorize("isAuthenticated()")
    fun changePassword(@RequestBody changePasswordDto: ChangePasswordDto) {
        userService.changePassword(changePasswordDto)
    }
}