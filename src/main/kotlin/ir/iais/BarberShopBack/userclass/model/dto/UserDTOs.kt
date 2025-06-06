@file:Suppress("unused")

package ir.iais.BarberShopBack.userclass.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import ir.iais.BarberShopBack.userclass.model.UserClass
import jakarta.validation.constraints.NotBlank
import java.io.Serializable

class UserByTokenRequest(
	@JsonProperty("api_token") @NotBlank val apiToken: String
)

open class BaseUserDTO(
	val phoneNumber: String,
	val roles: List<String>,
	val password: String,
	val username: String
) : Serializable

class CreateUserModalDTO(
	phoneNumber: String,
	roles: List<String>,
	password: String,
	username: String
) : BaseUserDTO(phoneNumber, roles, password, username)

class GetUserUpdateModalDTO(
	val id: Long,
	phoneNumber: String,
	roles: List<String>,
	password: String,
	username: String
) : BaseUserDTO(phoneNumber, roles, password, username)

class UpdateUserModalDTO(
	val id: Long,
	val phoneNumber: String,
	val birthDate: Long,
) : Serializable

class AuthUserModelDTO(
	val id: Long,
	val phoneNumber: String,
	val enabled: Boolean,
	val fullName: String?,
	val roles: List<String>,
) : Serializable

fun UserClass.toAuthUserModel(): AuthUserModelDTO {
	return AuthUserModelDTO(
		dbId!!,
		phoneNumber,
		enabled,
		fullName,
		roles,
	)
}

	class ChangeActivationDTO(val userId: Long, val status: Boolean)
	class UserRoleDTO(val id: Long, val name: String)
