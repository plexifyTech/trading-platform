package io.plexify.tradingplatform.auth.handler

import io.plexify.tradingplatform.auth.service.CsrfTokenExtractorService
import io.plexify.tradingplatform.auth.service.UserService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserHandler(
    private val userService: UserService,
    private val csrfTokenService: CsrfTokenExtractorService
) {
    suspend fun getUser(request: ServerRequest): ServerResponse {
        return userService.getUserDetails()
            .flatMap { userDto -> ok()
                .body(userDto.toMono())
            }
            .onErrorResume { mapErrorResponse(it) }
            .awaitSingle()
    }

    suspend fun getCsrfToken(request: ServerRequest): ServerResponse {
        val res = csrfTokenService.extractTokenFromCookie(request)
        return ok()
            .body(res)
            .awaitSingle()
    }

    private fun mapErrorResponse(exception: Throwable): Mono<ServerResponse> {
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue(exception.localizedMessage)
    }
}