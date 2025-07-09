package ir.iais.BarberShopBack.service.service

import ir.iais.BarberShopBack.service.repository.ServiceRepository
import org.springframework.stereotype.Service

@Service
class ServiceService(
	val serviceRepository: ServiceRepository
) {
	fun getServicesName(): List<String> {
		return serviceRepository.findServiceNames()
	}
}