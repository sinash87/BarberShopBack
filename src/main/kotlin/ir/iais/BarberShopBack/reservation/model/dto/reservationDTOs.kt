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

open class GetReservationForCreateDTO(
	val username: String,
	val phoneNumber: String,
	val startTime: Long,
	val endTime: Long,
	val active: Reservation.ReservationStatus,
	val services: List<String>
): Serializable

class GetReservationForUpdateDTO(
	val id: Long,
	username: String,
	phoneNumber: String,
	startTime: Long,
	endTime: Long,
	active: Reservation.ReservationStatus,
	services: List<String>
): GetReservationForCreateDTO(username , phoneNumber, startTime, endTime, active, services)


fun Reservation.toGetReservation(): GetReservationDTO {
	return GetReservationDTO(
		id!!,
		user!!.username,
		user!!.phoneNumber,
		startTime.toTimestamp(),
		status,
		services.map { it.toServiceDTO() }
	)
}


