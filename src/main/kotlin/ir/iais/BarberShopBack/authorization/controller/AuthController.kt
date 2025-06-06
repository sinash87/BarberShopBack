package ir.iais.BarberShopBack.authorization.controller

import ir.iais.BarberShopBack.authorization.model.dto.LoginRequestDTO
import ir.iais.BarberShopBack.authorization.service.AuthService
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@Validated @RequestBody body: LoginRequestDTO): JsonObject {
        return buildJsonObject {
            put("api_token", authService.login(body.username, body.password))
        }
    }

    @PostMapping("/logout", consumes = [MediaType.ALL_VALUE])
    fun logout(@RequestParam username: String, @RequestHeader(HttpHeaders.AUTHORIZATION) token: String) {
        val pureToken = token.replace("Bearer ", "")
        authService.logout(username, pureToken)
    }
}