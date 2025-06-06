package ir.iais.BarberShopBack.authorization.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration


@ConfigurationProperties("jwt")
open class JwtTokenProperties(
    @Value("\${jwt.secret}") val secret: String = "xZmw4Q7eL5CWaMcVEt8KhyU6f3JSrHNqs2knpdbFAuPBTX9zYgg6wr2bqZsxXyKuCJQfeUAYHPG5mkRWa4Mt8nDzN7ETcLBFvjpV", // todo 2: transfer it to safer place, like environment variable
    @Value("\${jwt.expiration-in-millis}") val expirationInMillis: Long = 3_600_000,
    @Value("\${jwt.expiration-duration-in-redis}") val expirationDurationInRedis: Duration = Duration.ofDays(1)
)