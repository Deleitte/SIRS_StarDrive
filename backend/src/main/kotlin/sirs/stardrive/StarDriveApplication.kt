package sirs.stardrive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import sirs.stardrive.config.RSAKeys

@SpringBootApplication
@EnableConfigurationProperties(RSAKeys::class)
class StarDriveApplication

fun main(args: Array<String>) {
	runApplication<StarDriveApplication>(*args)
}
