package ir.iais.BarberShopBack.userclass.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table
open class UserClass(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	open var dbId: Long? = null,
	private var username: String,
	open var phoneNumber: String,
	private var password: String,
	open var fullName: String,
	open var enabled: Boolean = true,
	@ElementCollection(fetch = FetchType.EAGER)
	open var roles: List<String> = mutableListOf()
) : UserDetails {

	@PrePersist
	@PreUpdate
	fun encryptPassword() {
		val encoder = BCryptPasswordEncoder()
		this.password = encoder.encode(this.password)
	}

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		return roles.map { SimpleGrantedAuthority(it) }.distinctBy { it.authority }.toMutableList()
	}

	override fun getPassword(): String = password

	override fun getUsername(): String = username

	override fun isEnabled(): Boolean = enabled
}