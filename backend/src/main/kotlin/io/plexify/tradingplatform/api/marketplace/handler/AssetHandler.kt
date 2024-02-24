package io.plexify.tradingplatform.api.marketplace.handler

import io.plexify.tradingplatform.api.marketplace.service.MockPriceRepository
import kotlinx.coroutines.reactive.asPublisher
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.sse

@Service
class AssetHandler(
    private val mockPriceRepository: MockPriceRepository
) {
    fun subscribeToPriceUpdates(request: ServerRequest) = ok()
        .sse()
        .body(mockPriceRepository.changePrices().asPublisher())

}