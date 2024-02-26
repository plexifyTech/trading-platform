package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.dto.AssetPriceUpdate
import io.plexify.tradingplatform.api.marketplace.service.PriceCalculator.generateStockPrice
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.springframework.stereotype.Service

@Service
class MockPriceRepository(
    private val sampleDataProvider: SampleDataProvider
) {

    fun changePrices(): Flow<AssetPriceUpdate> = flow {
        coroutineScope {
            while (isActive){
                sampleDataProvider.sampleData.forEach {
                    val newPrice = generateStockPrice(it.value.name)
                    emit(AssetPriceUpdate(it.key, newPrice))
                    it.value.prices.add(newPrice)
                }
                delay(1000)
            }
        }
    }

}