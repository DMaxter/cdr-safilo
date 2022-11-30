package com.casadosreclamos.service

import com.casadosreclamos.dto.*
import com.casadosreclamos.dto.OneFace
import com.casadosreclamos.model.Client
import com.casadosreclamos.model.User
import com.casadosreclamos.model.request.*
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.*
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject

private const val BRAND_NAME1 = "Test Brand1"
private const val BRAND_NAME2 = "Test Brand2"
private const val CLIENT_ID = 1234L
private const val CLIENT_BANNER = "A01"
private const val CLIENT_NAME = "Test Client"
private const val CLIENT_EMAIL = "testmail@gmail.com"
private const val CLIENT_FISCAL = "PT987654321"
private const val CLIENT_PHONE = "+987654321"
private const val CLIENT_ADDRESS = "Test Address"
private const val CLIENT_POSTAL = "1234-567"
private const val MATERIAL_NAME = "Test Material"
private const val IMAGE_LINK = "https://www.casadosreclamos.com/public/logo.png"
private const val USER_NAME = "Test User"
private const val USER_EMAIL = "testmail2@gmail.com"
private const val USER_PASSWORD = "123"
private const val USER_PLAFOND = 123.0
private const val PRICE_WIDTH = 10.0
private const val PRICE_HEIGHT = 20.0
private const val PRICE_COST = 100.0
private const val REQUEST_AMOUNT = 10
private const val REQUEST_OBS = "Test Observations"
private const val REQUEST_APPLICATION = true

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RequestServiceTest {
    @Inject
    lateinit var requestService: RequestService

    @Inject
    lateinit var brandService: BrandService

    @Inject
    lateinit var clientService: ClientService

    @Inject
    lateinit var materialService: MaterialService

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var priceService: PriceService

    lateinit var brand1: Brand

    lateinit var brand2: Brand

    lateinit var client: Client

    lateinit var material: Material

    lateinit var image1: Image

    lateinit var image2: Image

    lateinit var user: User


    @BeforeAll
    @ActivateRequestContext
    fun setup() {
        // Register a user
        user = authService.register(RegisterDto(USER_EMAIL, USER_NAME, USER_PASSWORD)).await().indefinitely()

        // Register two brands
        brand1 = brandService.add(BRAND_NAME1).await().indefinitely()
        brand2 = brandService.add(BRAND_NAME2).await().indefinitely()

        // Register an image for both brands
        image1 = brandService.addImage(brand1.id, IMAGE_LINK).await().indefinitely()
        image2 = brandService.addImage(brand2.id, IMAGE_LINK).await().indefinitely()

        // Register plafond for first brand
        userService.setPlafond(USER_EMAIL, brand1.id, USER_PLAFOND).await().indefinitely()

        // Register a material
        material = materialService.add(MATERIAL_NAME).await().indefinitely()

        // Register a price
        priceService.add(PriceDto(PRICE_WIDTH, PRICE_HEIGHT, material.id, PRICE_COST)).await().indefinitely()

        // Register a client
        client = clientService.add(
            ClientDto(
                CLIENT_ID,
                CLIENT_BANNER,
                CLIENT_NAME,
                CLIENT_FISCAL,
                CLIENT_EMAIL,
                CLIENT_PHONE,
                CLIENT_ADDRESS,
                CLIENT_POSTAL
            )
        ).await().indefinitely()
    }

    @Test
    @Order(1)
    fun testCreateRequestWithTabledPrice() {
        val measurements = Measurements()
        measurements.height = PRICE_HEIGHT
        measurements.width = PRICE_WIDTH

        val requestType = OneFace()
        requestType.cover = RequestSlotDto(ImageDto(image1), measurements, MaterialDto(material))

        val request = requestService.add(
            NewRequestDto(
                client.id,
                REQUEST_AMOUNT,
                REQUEST_OBS,
                REQUEST_APPLICATION,
                BrandDto(brand1),
                requestType
            ), USER_NAME
        ).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().item


        Assertions.assertNotNull(request)
        Assertions.assertNotEquals(0, request.id)
        Assertions.assertNotNull(request.cost)
        Assertions.assertEquals(PRICE_COST, request.cost)
        Assertions.assertNotNull(request.requester)
        Assertions.assertEquals(USER_EMAIL, request.requester.email)
        Assertions.assertNotNull(request.client)
        Assertions.assertEquals(CLIENT_ID, request.client.id)
        Assertions.assertEquals(RequestStatus.ORDERED, request.status)
        Assertions.assertNull(request.trackingCode)
        Assertions.assertNotNull(request.brand)
        Assertions.assertEquals(brand1.id, request.brand.id)
        Assertions.assertEquals(REQUEST_AMOUNT, request.amount)
        Assertions.assertEquals(REQUEST_APPLICATION, request.application)
        Assertions.assertNotNull(request.created)
        Assertions.assertNotNull(request.lastUpdate)
        Assertions.assertEquals(request.created, request.lastUpdate)
    }
}