package ir.iais.BarberShopBack.userclass.repository

import ir.iais.BarberShopBack.userclass.model.UserClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
interface UserRepo : JpaRepository<UserClass, Long>, JpaSpecificationExecutor<UserClass> {
	fun existsByUsername(username: String): Boolean
	fun findByUsername(username: String): UserClass?
	fun existsByPhoneNumber(phoneNumber: String): Boolean

	@Modifying
	@Transactional
	@Query("UPDATE UserClass u SET u.enabled = :enabled WHERE u.dbId = :id")
	fun updateUserEnabled(id: Long, enabled: Boolean)
}