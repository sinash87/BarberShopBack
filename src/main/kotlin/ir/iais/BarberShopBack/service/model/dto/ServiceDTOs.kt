package ir.iais.BarberShopBack.service.model.dto

import ir.iais.BarberShopBack.service.model.Service
import java.io.Serializable

class GetServiceDTO(
	val name: String,
	val price: Double,
): Serializable

fun Service.toServiceDTO(): GetServiceDTO {
	return GetServiceDTO(
		name,
		price
	)
}