package ir.iais.BarberShopBack.availability.model

import ir.iais.BarberShopBack.service.model.Service
import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
@Table
open class Availability(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)@Column(unique = true)
	open val id: Long = 0,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", nullable = false)
	open val service: Service,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	open val dayOfWeek: DayOfWeek,

	@Column(nullable = false)
	open val startTime: LocalTime,

	@Column(nullable = false)
	open val endTime: LocalTime
)