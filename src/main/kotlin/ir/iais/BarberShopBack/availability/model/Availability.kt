package ir.iais.BarberShopBack.availability.model

import ir.iais.BarberShopBack.service.model.Service
import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
@Table
data class Availability(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)@Column(unique = true)
	val id: Long = 0,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", nullable = false)
	val service: Service,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	val dayOfWeek: DayOfWeek,

	@Column(nullable = false)
	val startTime: LocalTime,

	@Column(nullable = false)
	val endTime: LocalTime
)