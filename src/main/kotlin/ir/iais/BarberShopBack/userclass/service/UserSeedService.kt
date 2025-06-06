package ir.iais.BarberShopBack.userclass.service

import ir.iais.BarberShopBack.userclass.repository.UserRepo
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserSeedService(
	private val userRepo: UserRepo,
	private val userCreateService: UserCreateService,
) {
	@EventListener(ApplicationReadyEvent::class)
	fun createUserIfEmpty() {
		if (userRepo.count() == 0L) {
			val adminUser = userCreateService.createUserObject(
				username = "admin@gmail.com",
				password = "a1157bG29",
				phoneNumber = "09368630582",
				fullName = "admin admini nasab",
				enabled = true,
				roles = emptyList()
			)
			userCreateService.createUser(adminUser)

			val systemUser = userCreateService.createUserObject(
				username = "system",
				password = "system",
				phoneNumber = "09120000000",
				fullName = "system systemian khah",
				enabled = true,
				roles = emptyList()
			)
			userCreateService.createUser(systemUser)
		}
	}
}
