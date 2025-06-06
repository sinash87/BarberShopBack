package ir.iais.BarberShopBack.utilities.filter

object FilterParser {
	fun parse(filters: Map<String, String>): List<FilterCriteria> {
		return filters.filterKeys { it.startsWith("filter.") }
			.mapNotNull { (key, value) ->
				if (value.isBlank()) return@mapNotNull null

				val field = key.removePrefix("filter.")
				val parts = value.split(":", limit = 2)

				if (parts.size < 2) return@mapNotNull null

				FilterCriteria(
					field = field,
					operator = parts[0].lowercase(),
					value = parts[1]
				)
			}
	}
}