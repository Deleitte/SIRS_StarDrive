package sirs.stardrive.config

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

enum class ErrorMessage(val message: String) {
    USER_NOT_FOUND("User not found"),
    ACCESS_DENIED("Access denied"),
    TEAM_NOT_FOUND("Team not found"),
    EMPLOYEE_NOT_FOUND("Employee not found"),
    EMPLOYEE_ALREADY_EXISTS("Employee already exists"),
    USER_ALREADY_EXISTS("User already exists"),
    PART_NOT_FOUND("Part not found"),
    PART_ALREADY_EXISTS("Part already exists"),
    ACTUATOR_ALREADY_EXISTS("Actuator already exists"),
    ACTUATOR_NOT_FOUND("Actuator not found"),
    SENSOR_ALREADY_EXISTS("Sensor already exists"),
    SENSOR_NOT_FOUND("Sensor not found"),
    OTP_ALREADY_EXISTS("Sensor not found"),
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class StarDriveException(val error: ErrorMessage, val value: String?) :
    RuntimeException(value?.let { "${error.message}: $it" } ?: error.message) {
    constructor(error: ErrorMessage) : this(error, null)
}

data class StarDriveExceptionDto(val message: String) {
    constructor(exception: StarDriveException) : this(exception.message!!)
}

@RestControllerAdvice
class StarDriveExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(StarDriveException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun stardriveException(e: StarDriveException): StarDriveExceptionDto =
        StarDriveExceptionDto(e)

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun accessDenied(e: AccessDeniedException): StarDriveExceptionDto =
        StarDriveExceptionDto(StarDriveException(ErrorMessage.ACCESS_DENIED))

    @ExceptionHandler(java.lang.Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun unexpectedException(e: Exception): StarDriveExceptionDto =
        StarDriveExceptionDto(e.message ?: "Unexpected error")
}
