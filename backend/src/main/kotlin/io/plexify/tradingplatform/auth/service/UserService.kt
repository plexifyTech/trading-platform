package io.plexify.tradingplatform.auth.service

import io.plexify.tradingplatform.auth.dto.UserDto
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class UserService {

    /**
     * Load the user from the ReactiveSecurityContextHolder to load KC-Credentials from the ReactiveOAuth2AuthorizedClientService
     * for Oidc userinfo endpoint access via API call.
     * Out of scope.
     * */
    fun getUser(): Mono<UserDto> {
        return Mono.just(UserDto("Ask keycloak"))
    }

}