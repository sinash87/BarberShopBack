package ir.iais.BarberShopBack.config

import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer

@Configuration
@Profile("dev")
class EmbeddedRedisConfiguration {
	private val redisServer: RedisServer = RedisServer.builder()
		.setting("maxheap 128M")
		.build()

	init {
		redisServer.start()
	}

	@Bean
	fun redisServer(): RedisServer = redisServer

	@PreDestroy
	fun stopRedis() {
		redisServer.stop()
	}
}