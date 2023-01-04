package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Document
data class User(
    val name: String,
    @Indexed(unique = true) @get:JvmName("user_name") val username: String,
    @get:JvmName("pass") var password: String,
    var role: Role,
    val totpKey: String?,
    var refreshToken: RefreshToken?,
    @MongoId val id: ObjectId? = null
) : UserDetails {
    constructor(newUserDto: NewUserDto) : this(
        newUserDto.name, newUserDto.username, newUserDto.password, Role.NEW, newUserDto.otpKey, null
    )

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

data class RefreshToken(val jti: String, val exp: Long)

data class NewUserDto(val name: String, val username: String, var password: String, val otpKey: String?)

data class UserDto(val name: String, val username: String, val role: Role) {
    constructor(user: User) : this(user.name, user.username, user.role)
}

data class ChangePasswordDto(val oldPassword: String, val newPassword: String)

data class TotpDto(val guess: Int)

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByUsername(username: String): User?
}

enum class Role {
    NEW,
    EMPLOYEE,
    ENGINEER,
    EXTERNAL,
    ADMIN
}
