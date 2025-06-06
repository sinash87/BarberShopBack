package ir.iais.BarberShopBack.authorization.service

import ir.iais.BarberShopBack.authorization.util.JwtTokenUtils
import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.userclass.service.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
	private val authenticationManager: AuthenticationManager,
	private val jwtTokenUtils: JwtTokenUtils,
	private val userService: UserService,
) {
	fun login(username: String, password: String): String {
		val authenticate = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
		val user = authenticate.principal as UserClass
		val token = jwtTokenUtils.generateToken(user.username)
		jwtTokenUtils.storeTokenInRedis(user, token)
		return token
	}

	fun logout(username: String, token: String) {
		val userClass = userService.getByUsername(username)
		if (jwtTokenUtils.extractUserName(token) != username) throw AccessDeniedException("Token $token does not match username")
		jwtTokenUtils.invalidateToken(userClass)
	}
}