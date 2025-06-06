package ir.iais.BarberShopBack.userclass.model.exceptions

import me.alidg.errors.annotation.ExceptionMapping
import me.alidg.errors.annotation.ExposeAsArg
import org.springframework.http.HttpStatus

@ExceptionMapping(errorCode = "user_not_found_exception", statusCode = HttpStatus.NOT_FOUND)
class UserNotFoundException(@ExposeAsArg(0) val username: String) : Exception()