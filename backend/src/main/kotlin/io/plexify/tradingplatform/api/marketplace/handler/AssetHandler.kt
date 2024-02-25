package io.plexify.tradingplatform.api.marketplace.handler

import io.plexify.tradingplatform.api.marketplace.dto.BuyRequest
import io.plexify.tradingplatform.api.marketplace.dto.BuyRequestResponse
import io.plexify.tradingplatform.api.marketplace.service.AssetTransactionService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono

@Service
class AssetHandler(
    private val assetTransactionService: AssetTransactionService
) {

    suspend fun getPortfolio(serverRequest: ServerRequest): ServerResponse {
        return ok().build().awaitSingle()
    }
    suspend fun buyAsset(serverRequest: ServerRequest): ServerResponse {
        val body = serverRequest.bodyToMono(BuyRequest::class.java).awaitSingle()
        val assetId = serverRequest.pathVariable("id")
        return assetTransactionService.handleBuyRequest(assetId, body)
            .flatMap { mapBuyAssetRes(it) }
            .awaitSingle()

    }

    private fun mapBuyAssetRes(data: BuyRequestResponse): Mono<ServerResponse> {
        val status = if (data.success){
            HttpStatus.OK
        } else {
            HttpStatus.BAD_REQUEST
        }
        return status(status).bodyValue(data)
    }
}