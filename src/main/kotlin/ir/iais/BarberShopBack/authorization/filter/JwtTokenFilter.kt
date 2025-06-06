package ir.iais.BarberShopBack.authorization.filter

import ir.iais.BarberShopBack.authorization.util.JwtTokenUtils
import ir.iais.BarberShopBack.userclass.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
	private val jwtTokenUtils: JwtTokenUtils,
	private val userService: UserService,
	private val redisTemplate: StringRedisTemplate,
) : OncePerRequestFilter() {
	private val excludedPaths = setOf("/v1/auth/login")
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val path = request.requestURI
		if (excludedPaths.contains(path)) {
			filterChain.doFilter(request, response)
			return
		}
		val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: ""
		if (!authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response)
			return
		}

		val token = authHeader.split(" ")[1].trim()

		val username = jwtTokenUtils.extractUserName(token)

		val userClass = userService.getByUsername(username)

		val oldToken = redisTemplate.opsForValue().get(jwtTokenUtils.generateUserJwtKey(username)) ?: ""
		if (oldToken != token) {
			filterChain.doFilter(request, response)
			return
		}

		val authentication = UsernamePasswordAuthenticationToken(userClass, null, userClass.authorities)
		authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

		SecurityContextHolder.getContext().authentication = authentication

		filterChain.doFilter(request, response)
	}
}