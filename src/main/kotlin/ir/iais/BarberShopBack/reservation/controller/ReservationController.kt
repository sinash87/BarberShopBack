package ir.iais.BarberShopBack.reservation.controller

import ir.iais.BarberShopBack.reservation.model.Reservation
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForCreateDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForUpdateDTO
import ir.iais.BarberShopBack.reservation.service.ReservationService
import ir.iais.BarberShopBack.utilities.filter.FilterParser
import ir.iais.BarberShopBack.utilities.filter.GenericSpecificationBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/reservation")
class ReservationController(
	private val reservationService: ReservationService,
) {
	@GetMapping(path = ["getReservations"])
	fun getReservation(
		@PageableDefault pageable: Pageable,
		@RequestParam filterParams: Map<String, String>
	): Page<GetReservationDTO> {
		val filters = FilterParser.parse(filterParams)
		val specification = GenericSpecificationBuilder.build<Reservation>(filters)
		return reservationService.getReservationWithFiltered(pageable, specification)
	}
	@GetMapping(path = ["get-reservation"])
	fun getReservationByID(@RequestParam id : Long): GetReservationForUpdateDTO {
		return reservationService.getReservationById(id)
	}
	@PostMapping(path = ["create-reservation"])
	fun createReservation(@RequestBody reservation: GetReservationForCreateDTO) {
		return reservationService.createReservation(reservation)
	}
	@PostMapping(path = ["update-reservation"])
	fun updateReservation(@RequestBody reservation: GetReservationForUpdateDTO) {
		return reservationService.updateReservation(reservation)
	}
	@GetMapping(path = ["get-reservation-userRole"])
	fun getReservationUserRole(@RequestParam id: Long): List<String> {
		return reservationService.getReservationUserRole(id)
	}
	@DeleteMapping(path = ["delete-reservation"])
	fun deleteReservation(@RequestParam id: Long) {
		return reservationService.deleteReservation(id)
	}
}