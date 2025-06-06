package ir.iais.BarberShopBack.userclass.service

import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.userclass.model.dto.CreateUserModalDTO
import ir.iais.BarberShopBack.userclass.model.exceptions.PhoneNumberAlreadyExistException
import ir.iais.BarberShopBack.userclass.model.exceptions.UserAlreadyExistException
import ir.iais.BarberShopBack.userclass.repository.UserRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*


@Service
class UserCreateService(
	private val userRepo: UserRepo,
) {

	@Transactional(rollbackFor = [Exception::class])
	fun createUser(user: CreateUserModalDTO): String {
		if (userRepo.existsByPhoneNumber(user.phoneNumber)) {
			throw PhoneNumberAlreadyExistException()
		}

		val password = PasswordGenerator.generatePassword()
		val userClass = UserClass(
			dbId = null,
			username = user.username,
			phoneNumber = user.phoneNumber,
			fullName = "",
			password = user.password,
			enabled = true,
			roles = user.roles,
		)
		createUser(userClass)
		return password
	}

	@Throws(UserAlreadyExistException::class)
	fun createUser(userClass: UserClass): UserClass {
		if (userRepo.existsByUsername(userClass.username)) {
			throw UserAlreadyExistException()
		}
		return userRepo.save(userClass)
	}

	fun createUserObject(
		username: String,
		password: String,
		phoneNumber: String,
		fullName: String,
		roles: List<String>,
		enabled: Boolean
	): UserClass {
		return UserClass(
			dbId = null,
			phoneNumber = phoneNumber,
			fullName = fullName,
			username = username,
			password = password,
			enabled = enabled,
			roles = roles
		)
	}

	object PasswordGenerator {

		private val random = SecureRandom()

		private const val UPPER = "ABCDEFGHJKMNPQRSTUVWXYZ"
		private const val LOWER = "abcdefghjkmnpqrstuvwxyz"
		private const val DIGITS = "23456789"

		fun generatePassword(): String {
			val upperChar = UPPER[random.nextInt(UPPER.length)]
			val lowerChar = LOWER[random.nextInt(LOWER.length)]
			val digitChars = (1..6).map { DIGITS[random.nextInt(DIGITS.length)] }

			val allChars = listOf(upperChar, lowerChar) + digitChars
			return allChars.shuffled(random).joinToString("")
		}
	}

}