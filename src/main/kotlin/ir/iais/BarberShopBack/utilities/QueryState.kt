@file:Suppress("unused")

package ir.iais.BarberShopBack.utilities

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class QueryState<T>(
    @Min(1) val page: Int,
    @Min(10) @Max(100) val size: Int,
    val sort: String?,
    val order: Order?,
    val filter: T?,
    val search: String?,
) {
    private fun getSort(): Sort? {
        if (sort == null || order == null) return null
        return Sort.by(if (order == Order.asc) Sort.Direction.ASC else Sort.Direction.DESC, sort)
    }

    fun getPageableWithOrder(): Pageable {
        return if (getSort() == null)
            PageRequest.of(
                page - 1,
                size,
            ) else {
            PageRequest.of(
                page - 1,
                size,
                getSort()!!,
            )
        }
    }
}

enum class Order {
    asc,
    desc
}