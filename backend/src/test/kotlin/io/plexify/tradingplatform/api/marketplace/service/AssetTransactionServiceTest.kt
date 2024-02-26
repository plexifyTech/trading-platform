package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import io.plexify.tradingplatform.api.marketplace.data.repository.MockAccountRepository
import io.plexify.tradingplatform.api.marketplace.dto.TransactionRequest
import io.plexify.tradingplatform.config.PathConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.kotlin.core.publisher.toMono

class AssetTransactionServiceTest {

    @Test
    fun `A liquid user performs a valid transaction`() {
        val dataProvider = SampleDataProvider()
        val account = Account(id = "1234")
        val service = service(account, dataProvider)
        val someAsset = dataProvider.sampleData.values.first()
        val initialAvailability = someAsset.availableAssets
        val request = TransactionRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        val purchased = account.portfolio.first()
        assertAll(
            { assertEquals(1000.0, account.balance) },
            { assertEquals(initialAvailability - 1, dataProvider.sampleData[someAsset.id]!!.availableAssets) },
            { assertEquals(1, account.portfolio.size) },
            { assertNotNull(account.findShare(someAsset.id)) },
            { assertEquals(99000.00, purchased.boughForPrice) },
            { assertEquals(someAsset.id, purchased.id) },
            { assertTrue(res!!.success) },
        )
    }

    @Test
    fun `A non-liquid user tries to buy an asset`() {
        val dataProvider = SampleDataProvider()
        val account = Account(id = "1234", balance = 0.00)
        val service = service(account, dataProvider)
        val someAsset = dataProvider.sampleData.values.first()
        val initialAvailability = someAsset.availableAssets
        val request = TransactionRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        assertAll(
            { assertEquals(0.00, account.balance) },
            { assertEquals(initialAvailability, dataProvider.sampleData[someAsset.id]!!.availableAssets) },
            { assertTrue(account.portfolio.isEmpty()) },
            { assertFalse(res!!.success) },
        )
    }

    @Test
    fun `can not buy unavailable asset`() {
        val dataProvider = SampleDataProvider()
        val someAsset = dataProvider.sampleData.values.first()
        someAsset.availableAssets = 0
        dataProvider.sampleData[someAsset.id] = someAsset
        val account = Account(id = "1234")
        val service = service(account, dataProvider)
        val request = TransactionRequest(price = 99000.00)

        val res = service.handleBuyRequest(someAsset.id, request).block()

        assertAll(
            { assertEquals(100000.00, account.balance) },
            { assertTrue(account.portfolio.isEmpty()) },
            { assertFalse(res!!.success) },
        )
    }

    @Test
    fun `can not buy non-existent asset`() {
        val dataProvider = SampleDataProvider()
        val account = Account(id = "1234")
        val service = service(account, dataProvider)
        val request = TransactionRequest(price = 99000.00)

        assertThrows(IllegalStateException::class.java) {
            service.handleBuyRequest("1234", request).block()
        }

    }

    private fun service(account: Account, sampleDataProvider: SampleDataProvider): AssetTransactionService {
        val mockMockRepo = mock(MockAccountRepository::class.java)
        `when`(mockMockRepo.getActiveAccount()).thenReturn(account.toMono())
        `when`(mockMockRepo.save(account)).thenReturn(account.toMono())
        val mockRequestContextProvider = mock(RequestContextProvider::class.java)
        `when`(mockRequestContextProvider.serverBaseUrl()).thenReturn("http://egal".toMono())
        return AssetTransactionService(
            userRepository = mockMockRepo,
            sampleDataProvider = sampleDataProvider,
            accountMappingService = AccountMappingService(
                mockAccountRepository = mockMockRepo,
                sampleDataProvider = sampleDataProvider,
                requestContextProvider = mockRequestContextProvider,
                pathConfig = PathConfig("/egal", "egal", "egal")
            )
        )
    }
}