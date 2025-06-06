package ir.iais.BarberShopBack.reservation.model

import ir.iais.BarberShopBack.service.model.Service
import ir.iais.BarberShopBack.userclass.model.UserClass
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
data class Reservation(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserClass,

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "reservation_services",
		joinColumns = [JoinColumn(name = "reservation_id")],
		inverseJoinColumns = [JoinColumn(name = "service_id")]
	)
	val services: MutableList<Service> = mutableListOf(),

	@Column(nullable = false)
	val startTime: LocalDateTime,

	@Column(nullable = false)
	val endTime: LocalDateTime,

	@Enumerated(EnumType.STRING)
	val status: ReservationStatus = ReservationStatus.ACTIVE,

	val createdAt: LocalDateTime = LocalDateTime.now()
){
	enum class ReservationStatus {
		ACTIVE, CANCELLED, COMPLETED
	}
}