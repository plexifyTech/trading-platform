package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.data.entity.User
import io.plexify.tradingplatform.api.marketplace.data.repository.MockUserRepository
import io.plexify.tradingplatform.api.marketplace.dto.BuyRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.kotlin.core.publisher.toMono

class AssetTransactionServiceTest {

    @Test
    fun `A liquid user performs a valid transaction`() {
        val dataProvider = SampleDataProvider()
        val user = User(id = "1234")
        val service = service(user, dataProvider)
        val someAsset = dataProvider.sampleData.values.first()
        val initialAvailability = someAsset.availableAssets
        val request = BuyRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        val purchased = user.portfolio.first()
        assertAll(
            { assertEquals(1000.0, user.balance) },
            { assertEquals(initialAvailability - 1, dataProvider.sampleData[someAsset.id]!!.availableAssets) },
            { assertEquals(1, user.portfolio.size) },
            { assertNotNull(user.findShare(someAsset.id)) },
            { assertEquals(99000.00, purchased.price) },
            { assertEquals(someAsset.id, purchased.id) },
            { assertTrue(res!!.success) },
        )
    }

    @Test
    fun `A non-liquid user tries to buy an asset`() {
        val dataProvider = SampleDataProvider()
        val user = User(id = "1234", balance = 0.00)
        val service = service(user, dataProvider)
        val someAsset = dataProvider.sampleData.values.first()
        val initialAvailability = someAsset.availableAssets
        val request = BuyRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        assertAll(
            { assertEquals(0.00, user.balance) },
            { assertEquals(initialAvailability, dataProvider.sampleData[someAsset.id]!!.availableAssets) },
            { assertTrue(user.portfolio.isEmpty()) },
            { assertFalse(res!!.success) },
        )
    }

    @Test
    fun `can not buy unavailable asset`() {
        val dataProvider = SampleDataProvider()
        val someAsset = dataProvider.sampleData.values.first()
        someAsset.availableAssets = 0
        dataProvider.sampleData[someAsset.id] = someAsset
        val user = User(id = "1234")
        val service = service(user, dataProvider)
        val request = BuyRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        assertAll(
            { assertEquals(100000.00, user.balance) },
            { assertTrue(user.portfolio.isEmpty()) },
            { assertFalse(res!!.success) },
        )
    }

    @Test
    fun `can not buy non-existent asset`() {
        val dataProvider = SampleDataProvider()
        val user = User(id = "1234")
        val service = service(user, dataProvider)
        val request = BuyRequest(price = 99000.00)

        assertThrows(IllegalStateException::class.java) {
            service.handleBuyRequest("1234", request).block()
        }

    }

    private fun service(user: User, sampleDataProvider: SampleDataProvider): AssetTransactionService {
        val mockMockRepo = mock(MockUserRepository::class.java)
        `when`(mockMockRepo.getActiveUser()).thenReturn(user.toMono())
        `when`(mockMockRepo.save(user)).thenReturn(user.toMono())
        return AssetTransactionService(userRepository = mockMockRepo, sampleDataProvider = sampleDataProvider)
    }
}