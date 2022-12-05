package sirs.stardrive

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Bruh(val bruh: String)

@RestController
class HelloController {
    @GetMapping("/")
    fun index(): Bruh = Bruh("bruh")
}