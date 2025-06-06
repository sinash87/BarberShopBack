package ir.iais.BarberShopBack.authorization.service

import ir.iais.BarberShopBack.userclass.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService


/**
 *
 * @author Vahid
 * 10/6/2024 9:09 AM
 */
class UserDetailsServiceImpl(private val userService: UserService) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = userService.getByUsername(username)

}