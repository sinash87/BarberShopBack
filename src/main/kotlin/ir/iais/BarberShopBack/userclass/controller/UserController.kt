package ir.iais.BarberShopBack.userclass.controller


import ir.iais.BarberShopBack.currentUser
import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.userclass.model.dto.*
import ir.iais.BarberShopBack.userclass.model.exceptions.PhoneNumberAlreadyExistException
import ir.iais.BarberShopBack.userclass.service.UserCreateService
import ir.iais.BarberShopBack.userclass.service.UserService
import ir.iais.BarberShopBack.utilities.filter.FilterParser
import ir.iais.BarberShopBack.utilities.filter.GenericSpecificationBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/user")
class UserController(
	private val userService: UserService,
	private val userCreateService: UserCreateService,
) {
	@GetMapping(path = [""])
	fun getUserByUsername(username: String): UserClass {
		return userService.getByUsername(username)
	}

	@GetMapping(path = ["/get-user"])
	fun getUserByUsernameForUpdate(username: String): GetUserUpdateModalDTO {
		return userService.getUserByUsername(username)
	}

	@PostMapping("/get-by-token")
	fun login(): AuthUserModelDTO {
		return currentUser().toAuthUserModel()
	}


	@GetMapping(path = ["/new-query"])
	fun getUserWithNewQuery(
		@PageableDefault pageable: Pageable,
		@RequestParam filterParams: Map<String, String>
	): Page<AuthUserModelDTO> {
		val filters = FilterParser.parse(filterParams)
		val specification = GenericSpecificationBuilder.build<UserClass>(filters)
		return userService.getUsersWithFiltered(pageable, specification)
	}

	@PutMapping("/change-activation")
	fun changeActivation(@RequestBody changeActivationDTO: ChangeActivationDTO) {
		userService.changeActivation(changeActivationDTO.userId, changeActivationDTO.status)
	}

	@PostMapping("/create-user")
	fun createUser(@RequestBody user: CreateUserModalDTO): ResponseEntity<String> { // todo 2: check ResponseEntity is good or not (default, it's not good idea)
		try {
			val generatedPassword = userCreateService.createUser(user)
			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(generatedPassword)
		} catch (e: PhoneNumberAlreadyExistException) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body("PHONE_NUMBER_ALREADY_EXIST")
		}
	}

	@PostMapping("/update-user")
	fun updateUser(@RequestBody user: UpdateUserModalDTO): ResponseEntity<String> { // todo 2: check ResponseEntity is good or not (default, it's not good idea)
		try {
			userService.updateUser(user)
			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("")
		} catch (e: PhoneNumberAlreadyExistException) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body("PHONE_NUMBER_ALREADY_EXIST")
		}
	}
}