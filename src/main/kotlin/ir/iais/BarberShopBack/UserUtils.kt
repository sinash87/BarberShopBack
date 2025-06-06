package ir.iais.BarberShopBack

import ir.iais.BarberShopBack.userclass.model.UserClass
import org.springframework.security.core.context.SecurityContextHolder


fun currentUser(): UserClass {
	return SecurityContextHolder.getContext().authentication.principal as UserClass
}



