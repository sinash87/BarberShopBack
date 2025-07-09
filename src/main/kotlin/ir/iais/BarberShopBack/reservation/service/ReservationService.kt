package ir.iais.BarberShopBack.reservation.service

import ir.iais.BarberShopBack.reservation.model.Reservation
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForCreateDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForUpdateDTO
import ir.iais.BarberShopBack.reservation.model.dto.toGetReservation
import ir.iais.BarberShopBack.reservation.repository.ReservationRepo
import ir.iais.BarberShopBack.service.repository.ServiceRepository
import ir.iais.BarberShopBack.toLocalDateTime
import ir.iais.BarberShopBack.toTimestamp
import ir.iais.BarberShopBack.userclass.model.UserClass
import ir.iais.BarberShopBack.userclass.repository.UserRepo
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReservationService(
	private val reservationRepo: ReservationRepo,
	private val serviceRepo: ServiceRepository,
	private val userRepo: UserRepo,
) {
	fun getReservationWithFiltered(pageable: Pageable, spec: Specification<Reservation>): Page<GetReservationDTO> {
		return reservationRepo.findAll(spec, pageable).map { it.toGetReservation() }
	}

	fun getReservationById(id: Long): GetReservationForUpdateDTO {
		val reservation: Reservation = reservationRepo.findById(id)
			.orElseThrow { throw EntityNotFoundException("Reservation with ID $id not found") }
		return GetReservationForUpdateDTO(
			id = reservation.id!!,
			username = reservation.user.username,
			phoneNumber = reservation.user.phoneNumber,
			startTime = reservation.startTime.toTimestamp(),
			endTime = reservation.endTime.toTimestamp(),
			active = reservation.status,
			services = reservation.services.map { it.name },
		)
	}

	@Transactional
	fun createReservation(reservation: GetReservationForCreateDTO) {
		val userClass = UserClass(
			dbId = null,
			username = reservation.username,
			phoneNumber = reservation.phoneNumber,
			password = "guest_password",
			fullName = reservation.username,
			enabled = false,
			roles = mutableListOf("guest")
		)
		val services = serviceRepo.findAllByNameIn(reservation.services).toMutableList()
		val reservationClass = Reservation(
			id = null,
			user = userClass,
			services = services,
			startTime = reservation.startTime.toLocalDateTime(),
			endTime = reservation.endTime.toLocalDateTime(),
			status = reservation.active,
			createdAt = LocalDateTime.now(),
		)
		reservationRepo.save(reservationClass)
	}
	@Transactional
	fun updateReservation(reservation: GetReservationForUpdateDTO){
		val existingReservation = reservationRepo.findById(reservation.id).orElseThrow {RuntimeException("Reservation with ID ${reservation.id} not found") }
		val services = serviceRepo.findAllByNameIn(reservation.services).toMutableList()
		existingReservation.user.username = reservation.username
		existingReservation.user.phoneNumber = reservation.phoneNumber
		existingReservation.services = services
		existingReservation.startTime = reservation.startTime.toLocalDateTime()
		existingReservation.endTime = reservation.endTime.toLocalDateTime()
		existingReservation.status = reservation.active
		reservationRepo.save(existingReservation)
	}

	fun getReservationUserRole(id: Long):List<String>{
		val reservation = reservationRepo.findById(id).orElseThrow {RuntimeException("Reservation with ID $id not found") }
		return reservation.user.roles
	}

	@Transactional
	fun deleteReservation(id: Long) {
		val reservation = reservationRepo.findById(id).orElseThrow {RuntimeException("Reservation with ID $id not found") }
		reservationRepo.deleteById(id)
		userRepo.deleteById(reservation.user.dbId!!)
	}
}