package sirs.stardrive

import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@Configuration
class SecurityConfiguration : VaadinWebSecurity()
