package com.casadosreclamos.service

import com.casadosreclamos.dto.RegisterDto
import com.casadosreclamos.model.Plafond
import com.casadosreclamos.model.User
import com.casadosreclamos.model.request.Brand
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject

private const val EMAIL = "testmail1@gmail.com"
private const val EMAIL_INVALID = "testmail"
private const val USER_NAME = "Test Name"
private const val PASSWORD = "123"
private const val BRAND_NAME = "Test Brand"
private const val PLAFOND1 = 100.0
private const val PLAFOND2 = 50.0

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var brandService: BrandService

    lateinit var user: User

    lateinit var brand: Brand

    @BeforeAll
    @ActivateRequestContext
    fun setup() {
        // Register a user and a brand
        authService.register(RegisterDto(EMAIL, USER_NAME, PASSWORD)).onItem().transformToUni { registered ->
            user = registered
            brandService.add(BRAND_NAME)
        }.onItem().invoke { registered ->
            brand = registered
        }.await().indefinitely()
    }

    @Test
    @Order(1)
    fun testSetPlafondNewBrand() {
        val expectedPlafond = Plafond()
        expectedPlafond.brand = brand
        expectedPlafond.user = user
        expectedPlafond.amount = PLAFOND1

        val user = userService.setPlafond(EMAIL, brand.id, PLAFOND1).await().indefinitely()
        Assertions.assertNotNull(user)
        Assertions.assertEquals(EMAIL, user.email)
        Assertions.assertEquals(USER_NAME, user.name)
        Assertions.assertEquals(1, user.credits.size)

        val plafond = user.credits.elementAt(0)

        Assertions.assertNotNull(plafond)
        Assertions.assertEquals(brand.id, plafond.brand.id)
        Assertions.assertEquals(brand.name, plafond.brand.name)
        Assertions.assertEquals(PLAFOND1, plafond.amount)
    }

    @Test
    @Order(2)
    fun testSetPlafondExistingBrand() {
        val expectedPlafond = Plafond()
        expectedPlafond.brand = brand
        expectedPlafond.user = user
        expectedPlafond.amount = PLAFOND2

        userService.setPlafond(EMAIL, brand.id, PLAFOND2).invoke { edited ->
            Assertions.assertNotNull(user)
            Assertions.assertEquals(EMAIL, edited.email)
            Assertions.assertEquals(USER_NAME, edited.name)
            Assertions.assertEquals(1, edited.credits.size)

            val plafond = edited.credits.elementAt(0)

            Assertions.assertNotNull(plafond)
            Assertions.assertEquals(brand.id, plafond.brand.id)
            Assertions.assertEquals(brand.name, plafond.brand.name)
            Assertions.assertEquals(PLAFOND2, plafond.amount)
        }.await().indefinitely()
    }

    @Test
    @Order(3)
    fun testUserInfoValid() {
        val expectedUser = User()
        expectedUser.name = USER_NAME
        expectedUser.email = EMAIL

        userService.getInfo(EMAIL).onItem().invoke { retrieved ->
            Assertions.assertNotNull(retrieved)
            Assertions.assertEquals(EMAIL, retrieved.email)
            Assertions.assertEquals(USER_NAME, retrieved.name)
            Assertions.assertEquals(1, retrieved.credits.size)

            val plafond = retrieved.credits.elementAt(0)

            Assertions.assertNotNull(plafond)
            Assertions.assertEquals(brand.id, plafond.brand.id)
            Assertions.assertEquals(brand.name, plafond.brand.name)
            Assertions.assertEquals(PLAFOND2, plafond.amount)
        }.await().indefinitely()
    }
}