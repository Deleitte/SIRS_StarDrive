package sirs.stardrive

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class HelloWorld(val message: String)

@RestController
class HelloController {
    @GetMapping("/")
    fun index() = HelloWorld("Hello World!")
}
