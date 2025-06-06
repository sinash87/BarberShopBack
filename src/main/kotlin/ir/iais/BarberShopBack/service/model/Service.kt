package ir.iais.BarberShopBack.service.model

import ir.iais.BarberShopBack.availability.model.Availability
import ir.iais.BarberShopBack.reservation.model.Reservation
import jakarta.persistence.*

@Entity
@Table
data class Service(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(unique = true)
	val id: Long = 0,

	@Column(nullable = false)
	val name: String,

	@Column(columnDefinition = "TEXT")
	val description: String? = null,

	val price: Double,
	val capacity: Int = 1,

	@Column(name = "duration_minutes")
	val durationMinutes: Int? = null,

	val isActive: Boolean = true,

	@ManyToMany(mappedBy = "services")
	val reservations: MutableList<Reservation> = mutableListOf(),

	@OneToMany(mappedBy = "service", cascade = [CascadeType.ALL], orphanRemoval = true)
	val availabilities: MutableList<Availability> = mutableListOf()
)