package sirs.stardrive.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.NewUserDto
import sirs.stardrive.models.Role
import sirs.stardrive.models.User
import sirs.stardrive.models.UserRepository
import java.util.*

@Service
class UserService(private val userRepository: UserRepository, passwordEncoder: BCryptPasswordEncoder) :
    UserDetailsService {
    init {
        // TODO: do this only in dev mode
        if (userRepository.findByUsername("admin") == null)
            userRepository.save(User("Vasco Correia", "admin", passwordEncoder.encode("admin"), Role.ADMIN))
    }

    @Throws(StarDriveException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, username)

    fun createUser(newUserDto: NewUserDto) {
        if (userRepository.findByUsername(newUserDto.username) != null)
            throw StarDriveException(ErrorMessage.USER_ALREADY_EXISTS, newUserDto.username)
        val encodedPassword = BCryptPasswordEncoder().encode(newUserDto.password)
        val newUser = NewUserDto(newUserDto.name, newUserDto.username, encodedPassword)
        userRepository.save(User(newUser))
    }
}