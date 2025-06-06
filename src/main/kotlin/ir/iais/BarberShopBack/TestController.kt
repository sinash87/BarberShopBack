package ir.iais.BarberShopBack

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
	fun test(request: HttpServletRequest): String {
		return request.remoteAddr
	}
}