package sirs.stardrive.models

data class LoginDtos(val username: String, val password: String)

data class LoginResponseDto(val token: String)
