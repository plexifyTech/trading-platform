package io.plexify.tradingplatform.auth.service

import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import io.plexify.tradingplatform.api.marketplace.data.repository.MockAccountRepository
import io.plexify.tradingplatform.auth.dto.UserDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: MockAccountRepository
) {

    /**
     * Load the user from the ReactiveSecurityContextHolder to load KC-Credentials from the ReactiveOAuth2AuthorizedClientService
     * for Oidc userinfo endpoint access via API call.
     * Out of scope.
     * */
    fun getUserDetails(): Mono<UserDto> {
        return Mono.just(UserDto("Ask keycloak"))
    }

    fun getUser(id: String): Mono<Account> {
        return userRepository.findById(id)
    }

    fun createUser(id: String): Mono<Account> {
        return userRepository.save(Account(id = id))
    }

}