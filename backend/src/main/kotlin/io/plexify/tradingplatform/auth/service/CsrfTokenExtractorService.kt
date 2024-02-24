package io.plexify.tradingplatform.auth.service

import io.plexify.tradingplatform.auth.dto.CsrfTokenResponse
import io.plexify.tradingplatform.auth.utils.isNotNullOrEmpty
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

@Service
class CsrfTokenExtractorService {
    fun extractTokenFromCookie(request: ServerRequest): Mono<CsrfTokenResponse> {
        val csrfToken = request.cookies().getFirst("XSRF-TOKEN")?.value
        return when {
            csrfToken.isNotNullOrEmpty() -> Mono.just(CsrfTokenResponse(csrfToken!!))
            else -> Mono.empty()
        }
    }
}