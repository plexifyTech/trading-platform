package io.plexify.tradingplatform.api.marketplace.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.context.ContextView

@Service
class RequestContextProvider(
    @Value("\${spring.webflux.base-path}")
    private val basePath: String = "",
) {
    fun serverBaseUrl(): Mono<String> {
        return Mono.deferContextual { data: ContextView -> Mono.just(data) }
            .map { contextView: ContextView ->
                val uri = contextView.get(ServerWebExchange::class.java).request.uri
                "${uri.scheme}://${uri.authority}$basePath"
            }
    }

}