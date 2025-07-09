package ir.iais.BarberShopBack.reservation.model

import ir.iais.BarberShopBack.service.model.Service
import ir.iais.BarberShopBack.userclass.model.UserClass
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
open class Reservation(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	open val id: Long? = 0,

	@ManyToOne(fetch = FetchType.LAZY , cascade = [(CascadeType.PERSIST)])
	@JoinColumn(name = "user_id")
	 open var user: UserClass,

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "reservation_services",
		joinColumns = [JoinColumn(name = "reservation_id")],
		inverseJoinColumns = [JoinColumn(name = "service_id")]
	)
	open var services: MutableList<Service> = mutableListOf(),

	open var startTime: LocalDateTime,

	@Column(nullable = false)
	open var endTime: LocalDateTime,

	@Enumerated(EnumType.STRING)
	open var status: ReservationStatus = ReservationStatus.ACTIVE,

	open val createdAt: LocalDateTime = LocalDateTime.now()
){
	enum class ReservationStatus {
		ACTIVE, CANCELLED, COMPLETED
	}
}