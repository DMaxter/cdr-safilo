package com.casadosreclamos.service

import com.casadosreclamos.dto.RegisterDto
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

private const val EMAIL_VALID1 = "testmail1@gmail.com"
private const val EMAIL_VALID2 = "testmail@gmail.com"
private const val EMAIL_INVALID = "testmail"
private const val NAME = "Test Name"
private const val PASSWORD = "123"

@QuarkusTest
class AuthServiceTest {
    @Inject
    lateinit var authService: AuthService

    @Test
    fun testRegisterValid() {
        val expected = User()
        expected.email = EMAIL_VALID1
        expected.name = NAME
        expected.password = "WON'T CHECK"
        expected.roles = mutableSetOf(Role.COMMERCIAL)
        expected.credits = mutableSetOf()

        authService.register(RegisterDto(EMAIL_VALID1, NAME, PASSWORD)).invoke { user ->
            Assertions.assertNotNull(user)
            Assertions.assertEquals(user.email, expected.email)
            Assertions.assertEquals(user.roles, expected.roles)
            Assertions.assertEquals(user.name, expected.name)
            Assertions.assertEquals(user.credits, expected.credits)
        }
    }
}