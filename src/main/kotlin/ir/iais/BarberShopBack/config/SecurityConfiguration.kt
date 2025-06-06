package ir.iais.BarberShopBack.config

import ir.iais.BarberShopBack.authorization.filter.JwtTokenFilter
import ir.iais.BarberShopBack.authorization.service.UserDetailsServiceImpl
import ir.iais.BarberShopBack.userclass.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtTokenFilter: JwtTokenFilter): SecurityFilterChain {
        http.csrf {
            it.disable()
        } // Disable CSRF protection
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .cors { cors ->
                cors.configurationSource { _ ->
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.allowCredentials = true
                    corsConfiguration.addAllowedOriginPattern("*")
                    corsConfiguration.addAllowedHeader("*")
                    corsConfiguration.addAllowedMethod("*")
                    corsConfiguration
                }
            }
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProviderBean(userService: UserService): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(UserDetailsServiceImpl(userService))
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManagerBean(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}