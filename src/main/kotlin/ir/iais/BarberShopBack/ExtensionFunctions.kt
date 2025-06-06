package ir.iais.BarberShopBack

import okhttp3.FormBody
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun String.isProbablyArabic(): Boolean {
    var i = 0
    while (i < this.length) {
        val c = this.codePointAt(i)
        if (c in 0x0600..0x06E0) {
            return true
        }
        i += Character.charCount(c)
    }
    return false
}

fun Map<String, String>.toFormBody(): FormBody {
    val builder = FormBody.Builder()
    this.forEach { (key, value) -> builder.add(key, value) }
    return builder.build()
}

fun LocalDateTime.toTimestamp(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
fun LocalDate.atStartOfDayTimestamp(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}