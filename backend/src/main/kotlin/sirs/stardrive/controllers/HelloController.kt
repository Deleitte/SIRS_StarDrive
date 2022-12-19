package sirs.stardrive.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class HelloWorld(val message: String)

@RestController
class HelloController {
    @GetMapping("/")
    fun index(): HelloWorld {
        return HelloWorld("Hello World!")
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun hello() = HelloWorld("Hello!")
}
