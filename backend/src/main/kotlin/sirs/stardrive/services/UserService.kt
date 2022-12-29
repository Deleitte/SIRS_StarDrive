package sirs.stardrive.services

import com.google.common.io.BaseEncoding
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.config.generateOtpKey
import sirs.stardrive.models.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val refreshTokenEncoder: BCryptPasswordEncoder
) :
    UserDetailsService {
    init {
        // TODO: do this only in dev mode
        if (userRepository.findByUsername("admin") == null)
            userRepository.save(
                User(
                    "Vasco Correia", "admin",
                    passwordEncoder.encode("admin"), Role.ADMIN, "JLXJKTYVWWE3TPRQQNM6SU2RHE======", null
                )
            )
    }

    @Throws(StarDriveException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, username)

    fun createUser(newUserDto: NewUserDto) {
        if (userRepository.findByUsername(newUserDto.username) != null)
            throw StarDriveException(ErrorMessage.USER_ALREADY_EXISTS, newUserDto.username)

        val encodedPassword = passwordEncoder.encode(newUserDto.password)
        val encodedTotpKey: String = BaseEncoding.base32().encode(generateOtpKey().encoded)

        val newUser = NewUserDto(newUserDto.name, newUserDto.username, encodedPassword, encodedTotpKey)
        userRepository.save(User(newUser))
    }

    fun changePassword(changePasswordDto: ChangePasswordDto) {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt;
        val user = userRepository.findByUsername(jwt.subject)
            ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, jwt.subject)

        if (!passwordEncoder.matches(changePasswordDto.oldPassword, user.password))
            throw StarDriveException(ErrorMessage.ACCESS_DENIED)

        user.password = passwordEncoder.encode(changePasswordDto.newPassword)
        userRepository.save(user)
    }

    @Throws(StarDriveException::class)
    fun hasRefreshToken(username: String): Boolean {
        val user = userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)
        return user.refreshToken != null
    }

    @Throws(StarDriveException::class)
    fun revokeRefreshToken(username: String) {
        val user = userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)
        user.refreshToken = null
        userRepository.save(user)
    }

    @Throws(StarDriveException::class)
    fun updateRefreshToken(username: String, refreshToken: String): String {
        val user = userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)
        user.refreshToken = refreshTokenEncoder.encode(refreshToken)
        userRepository.save(user)
        return refreshToken
    }

    @Throws(StarDriveException::class)
    fun validateRefreshToken(username: String, refreshToken: String): Boolean {
        val user = userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND)
        return refreshTokenEncoder.matches(
            refreshToken,
            user.refreshToken ?: throw StarDriveException(ErrorMessage.USER_NOT_LOGGED_IN)
        )
    }
}