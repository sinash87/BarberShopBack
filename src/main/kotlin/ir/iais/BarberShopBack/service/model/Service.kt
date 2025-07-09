package ir.iais.BarberShopBack.service.model

import ir.iais.BarberShopBack.availability.model.Availability
import ir.iais.BarberShopBack.reservation.model.Reservation
import jakarta.persistence.*

@Entity
@Table
open class Service(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(unique = true)
	open val id: Long = 0,

	@Column(nullable = false)
	open val name: String,

	@Column(columnDefinition = "TEXT")
	open val description: String? = null,

	open val price: Double,
	open val capacity: Int = 1,

	@Column(name = "duration_minutes")
	open val durationMinutes: Int? = null,

	open val isActive: Boolean = true,

	@ManyToMany(mappedBy = "services")
	open val reservations: MutableList<Reservation> = mutableListOf(),

	@OneToMany(mappedBy = "service", cascade = [CascadeType.ALL], orphanRemoval = true)
	open val availabilities: MutableList<Availability> = mutableListOf()
)