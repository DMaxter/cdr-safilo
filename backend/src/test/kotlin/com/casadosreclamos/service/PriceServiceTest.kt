package com.casadosreclamos.service

import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.model.request.Material
import com.casadosreclamos.model.request.Measurements
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.*
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Inject

private const val MATERIAL_NAME = "Test Material"
private const val COST1 = 123.0
private const val COST2 = 999.0
private const val HEIGHT = 10.0
private const val WIDTH = 20.0

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PriceServiceTest {
    @Inject
    lateinit var priceService: PriceService

    @Inject
    lateinit var materialService: MaterialService

    lateinit var material: Material

    @BeforeAll
    @ActivateRequestContext
    fun setup() {
        // Register a material
        material = materialService.add(MATERIAL_NAME).await().indefinitely()
    }

    @Test
    @Order(1)
    fun testRegisterValidPrice() {
        val price = priceService.add(PriceDto(WIDTH, HEIGHT, material.id, COST1)).await().indefinitely()

        Assertions.assertNotNull(price)
        Assertions.assertNotNull(price.id)
        Assertions.assertNotNull(price.id.measurements)
        Assertions.assertEquals(HEIGHT, price.id.measurements.height)
        Assertions.assertEquals(WIDTH, price.id.measurements.width)
        Assertions.assertEquals(material.id, price.id.materialId)
        Assertions.assertEquals(COST1, price.cost)
    }

    @Test
    @Order(2)
    fun testEditValidPrice() {
        val price = priceService.edit(PriceDto(WIDTH, HEIGHT, material.id, COST2)).await().indefinitely()

        Assertions.assertNotNull(price)
        Assertions.assertNotNull(price.id)
        Assertions.assertNotNull(price.id.measurements)
        Assertions.assertEquals(HEIGHT, price.id.measurements.height)
        Assertions.assertEquals(WIDTH, price.id.measurements.width)
        Assertions.assertEquals(material.id, price.id.materialId)
        Assertions.assertEquals(COST2, price.cost)
    }

    @Test
    @Order(3)
    fun testFindValidPrice() {
        val measurements = Measurements()
        measurements.height = HEIGHT
        measurements.width = WIDTH

        val price = priceService.get(measurements, material.id).await().indefinitely()

        Assertions.assertNotNull(price)
        Assertions.assertNotNull(price.id)
        Assertions.assertNotNull(price.id.measurements)
        Assertions.assertEquals(HEIGHT, price.id.measurements.height)
        Assertions.assertEquals(WIDTH, price.id.measurements.width)
        Assertions.assertEquals(material.id, price.id.materialId)
        Assertions.assertEquals(COST2, price.cost)
    }

    @Test
    @Order(4)
    fun testDeleteValidPrice() {
        priceService.delete(PriceDto(WIDTH, HEIGHT, material.id, null)).await().indefinitely()

        val measurements = Measurements()
        measurements.height = HEIGHT
        measurements.width = WIDTH

        val price = priceService.get(measurements, material.id).await().indefinitely()

        Assertions.assertNull(price)
    }
}