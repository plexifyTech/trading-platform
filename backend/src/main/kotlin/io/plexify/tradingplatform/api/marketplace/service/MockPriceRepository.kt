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
                    emit(AssetPriceUpdate(it.key, generateStockPrice(it.value.name)))
                }
                delay(1000)
            }
        }
    }

}