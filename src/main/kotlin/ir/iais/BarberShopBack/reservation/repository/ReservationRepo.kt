package ir.iais.BarberShopBack.reservation.repository

import ir.iais.BarberShopBack.reservation.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepo: JpaRepository<Reservation, Long> , JpaSpecificationExecutor<Reservation> {
}