package io.plexify.tradingplatform.api.marketplace.handler

import io.plexify.tradingplatform.api.marketplace.dto.TransactionRequest
import io.plexify.tradingplatform.api.marketplace.dto.TransactionResponse
import io.plexify.tradingplatform.api.marketplace.service.AccountMappingService
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
    private val assetTransactionService: AssetTransactionService,
    private val accountMappingService: AccountMappingService
) {
    suspend fun getAccountData(serverRequest: ServerRequest): ServerResponse {
        return accountMappingService.mapActiveAccount()
            .flatMap { ok().bodyValue(it) }
            .awaitSingle()
    }

    suspend fun buyAsset(serverRequest: ServerRequest): ServerResponse {
        val body = serverRequest.bodyToMono(TransactionRequest::class.java).awaitSingle()
        val assetId = serverRequest.pathVariable("id")
        return assetTransactionService.handleBuyRequest(assetId, body)
            .flatMap { mapBuyAssetRes(it) }
            .awaitSingle()
    }

    suspend fun sellAsset(serverRequest: ServerRequest): ServerResponse {
        val assetId = serverRequest.pathVariable("id")
        return assetTransactionService.handleSellRequest(assetId)
            .flatMap { mapBuyAssetRes(it)}
            .awaitSingle()
    }

    private fun mapBuyAssetRes(data: TransactionResponse): Mono<ServerResponse> {
        val status = if (data.success) {
            HttpStatus.OK
        } else {
            HttpStatus.BAD_REQUEST
        }
        return status(status).bodyValue(data)
    }
}