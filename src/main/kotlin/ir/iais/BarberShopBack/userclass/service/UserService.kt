package ir.iais.BarberShopBack.userclass.service

import ir.iais.BarberShopBack.authorization.util.JwtTokenUtils
import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.userclass.model.dto.*
import ir.iais.BarberShopBack.userclass.model.exceptions.PhoneNumberAlreadyExistException
import ir.iais.BarberShopBack.userclass.model.exceptions.UserNotFoundException
import ir.iais.BarberShopBack.userclass.repository.UserRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
	private val userRepo: UserRepo,
	private val jwtTokenUtils: JwtTokenUtils,
) {


	@Throws(UserNotFoundException::class)
	fun getByUsername(username: String): UserClass {
		return userRepo.findByUsername(username) ?: throw UserNotFoundException(username)
	}

	@Throws(UserNotFoundException::class)
	fun getUserByUsername(username: String): GetUserUpdateModalDTO {
		val userClass = userRepo.findByUsername(username) ?: throw UserNotFoundException(username)
		return GetUserUpdateModalDTO(
			id = userClass.dbId!!,
			phoneNumber = userClass.phoneNumber,
			password = userClass.password,
			roles = userClass.roles,
			username = userClass.username,
		)
	}

	@Throws(UserNotFoundException::class)
	fun getByToken(token: String): UserClass {
		val username = jwtTokenUtils.extractUserName(token)
		return getByUsername(username)
	}

	fun getUsersWithFiltered(pageable: Pageable, spec: Specification<UserClass>): Page<AuthUserModelDTO> {
		return userRepo.findAll(spec, pageable).map { it.toAuthUserModel() }
	}

	fun changeActivation(userId: Long, status: Boolean) {
		userRepo.updateUserEnabled(userId, status)
		if (!status) {
			val user = userRepo.findById(userId)
				.orElseThrow { IllegalArgumentException("User not found with ID: $userId") }
			jwtTokenUtils.invalidateToken(user)
		}
	}

	@Transactional(rollbackFor = [Exception::class])
	fun updateUser(user: UpdateUserModalDTO) {
		val existingUser = userRepo.findById(user.id).orElseThrow { RuntimeException("User not found") }
		if (user.phoneNumber != existingUser.phoneNumber && userRepo.existsByPhoneNumber(user.phoneNumber)) {
			throw PhoneNumberAlreadyExistException()
		}
		existingUser.phoneNumber = user.phoneNumber
		existingUser.fullName = ""
		userRepo.save(existingUser)
	}
}
