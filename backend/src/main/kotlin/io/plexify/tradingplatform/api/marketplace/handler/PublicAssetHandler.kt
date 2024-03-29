package io.plexify.tradingplatform.api.marketplace.handler

import io.plexify.tradingplatform.api.MpApiConfig.Companion.ASSET_PATH
import io.plexify.tradingplatform.api.marketplace.dto.AssetFields
import io.plexify.tradingplatform.api.marketplace.dto.AssetResponse
import io.plexify.tradingplatform.api.marketplace.service.MockPriceRepository
import io.plexify.tradingplatform.api.marketplace.service.RequestContextProvider
import io.plexify.tradingplatform.api.marketplace.service.SampleDataProvider
import io.plexify.tradingplatform.config.PathConfig
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.sse

@Service
class PublicAssetHandler(
    private val mockPriceRepository: MockPriceRepository,
    private val sampleDataProvider: SampleDataProvider,
    private val requestContextProvider: RequestContextProvider,
    private val pathConfig: PathConfig
) {
    fun subscribeToPriceUpdates(request: ServerRequest) = ok()
        .sse()
        .body(mockPriceRepository.changePrices().asPublisher())

    suspend fun listAssets(request: ServerRequest): ServerResponse {
        val data = sampleDataProvider.sampleData
            .map { entry ->
                AssetResponse(
                    id = entry.key,
                    fields = AssetFields(
                        details = entry.value,
                        buyUrl = buyUrl(entry.key)
                    )
                )
            }
        return ok()
            .bodyValue(data)
            .awaitSingle()
    }

    private suspend fun buyUrl(id: String): String {
        val baseUrl = requestContextProvider.serverBaseUrl().awaitSingle()
        return "$baseUrl${pathConfig.apiBasePath}/$ASSET_PATH/$id/buy"
    }


}