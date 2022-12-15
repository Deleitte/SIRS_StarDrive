package sirs.stardrive.auth

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import java.util.*

@Document
data class User(
    @Indexed(unique = true) @get:JvmName("name") val username: String,
    @get:JvmName("pass") val password: String,
    val role: Role,
    @MongoId val id: ObjectId? = null
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByUsername(username: String): User?
}

enum class Role {
    EMPLOYEE,
    ENGINEER,
    EXTERNAL,
    ADMIN
}
