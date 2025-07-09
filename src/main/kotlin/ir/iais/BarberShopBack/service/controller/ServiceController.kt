package ir.iais.BarberShopBack.service.controller

import ir.iais.BarberShopBack.service.service.ServiceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/service")
class ServiceController(
	val serviceService: ServiceService
)

{
	@GetMapping(path = ["getServicesName"])
	fun getServicesName():List<String>{
		return serviceService.getServicesName()
	}
}