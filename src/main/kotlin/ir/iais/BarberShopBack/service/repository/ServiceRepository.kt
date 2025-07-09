package ir.iais.BarberShopBack.service.repository

import ir.iais.BarberShopBack.service.model.Service
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository: JpaRepository<Service, Long> , JpaSpecificationExecutor<Service>{
	@Query("SELECT s.name FROM Service s")
	fun findServiceNames(): List<String>

	fun findAllByNameIn(name: List<String>): List<Service>
}