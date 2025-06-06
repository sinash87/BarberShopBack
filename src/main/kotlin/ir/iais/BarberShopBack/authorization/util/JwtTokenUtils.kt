package ir.iais.BarberShopBack.authorization.util

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import io.jsonwebtoken.security.WeakKeyException
import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.authorization.controller.JwtTokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.redis.connection.RedisStringCommands
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.types.Expiration
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
@EnableConfigurationProperties(JwtTokenProperties::class)
class JwtTokenUtils(
    private val redisTemplate: StringRedisTemplate,
    private val jwtTokenProperties: JwtTokenProperties
) {
    companion object {
        internal const val JWT_KEY_PREFIX = "JWT:"

        internal const val USER_JWT_KEY_PREFIX = "USER:JWT:"

        internal const val JWT_LAST_USE_TIME_KEY_PREFIX = "JWT:LAST_USE:"
    }

    fun generateJwtKey(jwtToken: String): String = "$JWT_KEY_PREFIX$jwtToken"

    fun generateUserJwtKey(username: String): String = "$USER_JWT_KEY_PREFIX$username"

    fun generateJwtLastUsedKey(jwtToken: String): String = "$JWT_LAST_USE_TIME_KEY_PREFIX$jwtToken"

    fun generateToken(userName: String?): String {
        return generateToken(userName, jwtTokenProperties.expirationInMillis)
    }

    fun generateToken(userName: String?, expireAfterMillis: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        return Jwts.builder()
            .subject(userName)
            .issuedAt(Date(currentTimeMillis))
            .expiration(Date(currentTimeMillis + expireAfterMillis))
            .signWith(publicSignInKey())
            .compact()
    }

    fun validateToken(token: String) {
        extractAllClaims(token)
    }

    fun extractExpiration(token: String): Date = extractAllClaims(token).expiration

    fun extractUserName(token: String): String = extractAllClaims(token).subject

    @Throws(
        ExpiredJwtException::class,
        UnsupportedJwtException::class,
        MalformedJwtException::class,
        SignatureException::class,
        IllegalArgumentException::class
    )
    private fun extractAllClaims(token: String): Claims =
        Jwts.parser().verifyWith(publicSignInKey()).build().parseSignedClaims(token).payload

    @Throws(WeakKeyException::class)
    private fun publicSignInKey(): SecretKey? =
        Keys.hmacShaKeyFor(jwtTokenProperties.secret.toByteArray(StandardCharsets.UTF_8))

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    fun storeTokenInRedis(userClass: UserClass, token: String) {
        val oldToken = redisTemplate.opsForValue().get(generateUserJwtKey(userClass.username))
        redisTemplate.executePipelined { connection ->
            if (oldToken != null) connection.keyCommands().del(generateJwtKey(oldToken).toByteArray())

            val expiration = Expiration.from(jwtTokenProperties.expirationDurationInRedis)
            val jwtKey = generateJwtKey(token).toByteArray()
            connection.stringCommands().set(
                jwtKey,
                userClass.dbId.toString().toByteArray(),
                expiration,
                RedisStringCommands.SetOption.UPSERT
            )

            val userJwtKey = generateUserJwtKey(userClass.username).toByteArray()
            connection.stringCommands()
                .set(userJwtKey, token.toByteArray(), expiration, RedisStringCommands.SetOption.UPSERT)

            null
        }
    }

    fun invalidateToken(userClass: UserClass) {
        val oldToken = redisTemplate.opsForValue().get(generateUserJwtKey(userClass.username)) ?: return
        redisTemplate.executePipelined { connection ->
            connection.keyCommands().del(generateJwtKey(oldToken).toByteArray())
            val userJwtKey = generateUserJwtKey(userClass.username).toByteArray()
            connection.keyCommands().del(userJwtKey)

            null
        }
    }
}