package sirs.stardrive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StardriveApplication

fun main(args: Array<String>) {
	runApplication<StardriveApplication>(*args)
}
