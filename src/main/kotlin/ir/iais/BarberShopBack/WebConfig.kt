package ir.iais.BarberShopBack

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat
import java.util.*


@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(String::class.java, Date::class.java) { source ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            try {
                dateFormat.parse(source)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid date format. Please use this pattern\"" + dateFormat.toPattern() + "\"")
            }
        }
    }
}