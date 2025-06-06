package ir.iais.BarberShopBack.userclass.repository.specification

import ir.iais.BarberShopBack.userclass.model.UserClass
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class UserTableSpecification(
	val search: String,
) : Specification<UserClass> {
	override fun toPredicate(
		root: Root<UserClass?>,
		query: CriteriaQuery<*>?,
		c: CriteriaBuilder
	): Predicate? {
		return c.or(c.like(root.get<String>("fullName"), search), c.like(root.get<String>("username"), search))
	}
}