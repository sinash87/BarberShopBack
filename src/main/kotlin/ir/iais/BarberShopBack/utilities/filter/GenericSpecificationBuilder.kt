package ir.iais.BarberShopBack.utilities.filter

import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime

object GenericSpecificationBuilder {
	fun <T> build(filters: List<FilterCriteria>): Specification<T> {
		return Specification { root, _, cb ->
			val predicates = filters.mapNotNull { filter ->
				val path: Path<*> = resolvePath(root, filter.field)

				when (filter.operator) {
					"eq" -> cb.equal(path, filter.value)
					"like" -> cb.like(cb.lower(path as Path<String>), "%${filter.value.lowercase()}%")
					"in" -> {
						val values = filter.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
						path.`in`(values)
					}

					"gt" -> {
						val typedValue = convertToComparableValue(path.javaType, filter.value)
						if (typedValue != null) cb.greaterThan(path as Path<Comparable<Any>>, typedValue)
						else null
					}

					"lt" -> {
						val typedValue = convertToComparableValue(path.javaType, filter.value)
						if (typedValue != null) cb.lessThan(path as Path<Comparable<Any>>, typedValue)
						else null
					}

					else -> null
				}
			}

			cb.and(*predicates.toTypedArray())
		}
	}

	// Support for nested fields (e.g. "user.name")
	private fun <T> resolvePath(root: Root<T>, field: String): Path<*> {
		return field.split(".").fold(root as Path<*>) { path, part -> path.get<Any>(part) }
	}

	fun convertToComparableValue(type: Class<*>, value: String): Comparable<Any>? {
		return try {
			when (type) {
				Int::class.java, Integer::class.java -> value.toInt()
				Long::class.java, java.lang.Long::class.java -> value.toLong()
				Double::class.java, java.lang.Double::class.java -> value.toDouble()
				LocalDate::class.java -> LocalDate.parse(value)
				LocalDateTime::class.java -> LocalDateTime.parse(value)
				else -> null // unsupported or non-comparable type
			} as? Comparable<Any>
		} catch (_: Exception) {
			null
		}
	}
}