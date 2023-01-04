package sirs.stardrive.models

data class LoginRequestDto(val username: String, val password: String)

data class LoginResponseDto(val token: String)

data class RegisterResponseDto(val token: String, val totpSecret: String)
