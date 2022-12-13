package sirs.stardrive.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

private const val MAX_AGE_SECS: Long = 3600

@Configuration
class WebConfiguration : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
            .maxAge(MAX_AGE_SECS)
    }
}