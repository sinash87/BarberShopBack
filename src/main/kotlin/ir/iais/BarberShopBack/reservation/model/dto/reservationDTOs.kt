package ir.iais.BarberShopBack.reservation.model.dto

import ir.iais.BarberShopBack.reservation.model.Reservation
import ir.iais.BarberShopBack.service.model.dto.GetServiceDTO
import ir.iais.BarberShopBack.service.model.dto.toServiceDTO
import ir.iais.BarberShopBack.toTimestamp
import java.io.Serializable

class GetReservationDTO(
	val id: Long,
	val username: String,
	val phoneNumber: String,
	val visitTime: Long,
	val active: Reservation.ReservationStatus,
	val services: List<GetServiceDTO>
): Serializable

class GetReservationForUpdateDTO(
	username: String,
	phoneNumber: String,
	visitTime: Long,
	active: Reservation.ReservationStatus,
	services: List<String>
)


fun Reservation.toGetReservation(): GetReservationDTO {
	return GetReservationDTO(
		id,
		user.username,
		user.phoneNumber,
		startTime.toTimestamp(),
		status,
		services.map { it.toServiceDTO() }
	)
}


