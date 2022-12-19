package sirs.stardrive.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Document
data class Team(
    @Indexed(unique = true) val name: String,
    val salary: Int,
    @DocumentReference val employees: List<Employee> = emptyList(),
    @MongoId val id: ObjectId? = null
) {
    constructor(newTeamDto: NewTeamDto) : this(newTeamDto.name, newTeamDto.salary)
}

data class NewTeamDto(val name: String, val salary: Int)

data class TeamDto(val name: String, val salary: Int, val employees: List<EmployeeDto>) {
    constructor(team: Team) : this(team.name, team.salary, team.employees.map { EmployeeDto(it) })
}

@Repository
interface TeamRepository : MongoRepository<Team, ObjectId> {
    fun findByName(name: String): Team?
}

@Document
data class Employee(
    @Indexed(unique = true) @DocumentReference val user: User,
    val name: String,
    @DocumentReference val team: Team,
    @MongoId val id: ObjectId? = null
)

data class EmployeeDto(val name: String, val username: String, val team: String) {
    constructor(employee: Employee) : this(employee.name, employee.user.username, employee.team.name)
}

@Repository
interface EmployeeRepository : MongoRepository<Employee, ObjectId> {
    fun findByUser(user: User): Employee?
}
