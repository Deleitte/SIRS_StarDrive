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
import sirs.stardrive.models.ChangePasswordDto
import sirs.stardrive.models.LoginRequestDto
import sirs.stardrive.models.LoginResponseDto
import sirs.stardrive.models.NewUserDto
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

    @PostMapping("/token")
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

    @PostMapping("/token/refresh")
    fun refreshToken(
        request: HttpServletRequest,
        @CookieValue(name = "refresh-token") refreshToken: String
    ): LoginResponseDto {
        val authentication = refreshTokenAuthProvider.authenticate(BearerTokenAuthenticationToken(refreshToken))
        return LoginResponseDto(tokenService.renewAccessToken(authentication, refreshToken))
    }

    @PostMapping("/token/revoke")
    @PreAuthorize("isAuthenticated()")
    fun revokeToken() {
        // TODO this probably shouldn't be like this
        val authentication = SecurityContextHolder.getContext().authentication!!
        tokenService.revokeRefreshToken(authentication)
    }

    @PostMapping("/register")
    fun register(@RequestBody newUserDto: NewUserDto): LoginResponseDto {
        userService.createUser(newUserDto)
        val authenticationToken = UsernamePasswordAuthenticationToken(newUserDto.username, newUserDto.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        val token = tokenService.generateAccessToken(authentication)
        return LoginResponseDto(token)
    }

    @PostMapping("/changepassword")
    @PreAuthorize("isAuthenticated()")
    fun changePassword(@RequestBody changePasswordDto: ChangePasswordDto) {
        userService.changePassword(changePasswordDto)
    }
}