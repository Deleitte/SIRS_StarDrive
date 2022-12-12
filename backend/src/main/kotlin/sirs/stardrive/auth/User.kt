package sirs.stardrive.auth

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import sirs.stardrive.ErrorMessage
import sirs.stardrive.StarDriveException


@Document
data class User(
    @Id val id: String,
    @Indexed(unique = true) @get:JvmName("name") val username: String,
    @get:JvmName("pass") val password: String,
    val role: Role
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
}

enum class Role {
    EMPLOYEE,
    ENGINEER,
    EXTERNAL,
    ADMIN
}

@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService {
    fun getUser(username: String): User? = userRepository.findByUsername(username)

    override fun loadUserByUsername(username: String): UserDetails =
        getUser(username) ?: throw StarDriveException(ErrorMessage.USER_NOT_FOUND, username)
}
