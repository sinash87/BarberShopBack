package ir.iais.BarberShopBack.reservation.service

import ir.iais.BarberShopBack.reservation.model.Reservation
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForUpdateDTO
import ir.iais.BarberShopBack.reservation.model.dto.toGetReservation
import ir.iais.BarberShopBack.reservation.repository.ReservationRepo
import ir.iais.BarberShopBack.toTimestamp
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class ReservationService(
	private val reservationRepo: ReservationRepo
) {
	fun getReservationWithFiltered(pageable: Pageable, spec: Specification<Reservation>): Page<GetReservationDTO> {
		return reservationRepo.findAll(spec, pageable).map { it.toGetReservation() }
	}

	fun getReservationById(id: Long): GetReservationForUpdateDTO {
		val reservation: Reservation = reservationRepo.findById(id)
			.orElseThrow { throw EntityNotFoundException("Reservation with ID $id not found") }
		return GetReservationForUpdateDTO(
			username = reservation.user.username,
			phoneNumber = reservation.user.phoneNumber,
			visitTime = reservation.startTime.toTimestamp(),
			active = reservation.status,
			services = reservation.services.map { it.name },
		)
	}
}