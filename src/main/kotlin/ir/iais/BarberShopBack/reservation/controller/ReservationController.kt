package ir.iais.BarberShopBack.reservation.controller

import ir.iais.BarberShopBack.reservation.model.Reservation
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationDTO
import ir.iais.BarberShopBack.reservation.model.dto.GetReservationForUpdateDTO
import ir.iais.BarberShopBack.reservation.service.ReservationService
import ir.iais.BarberShopBack.utilities.filter.FilterParser
import ir.iais.BarberShopBack.utilities.filter.GenericSpecificationBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}