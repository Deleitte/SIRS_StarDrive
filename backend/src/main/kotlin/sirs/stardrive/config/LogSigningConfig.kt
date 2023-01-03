package sirs.stardrive.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("log")
class LogSigningConfig(val publicKey: String, val privateKey: String)