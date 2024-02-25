package io.plexify.tradingplatform.auth.service

import io.plexify.tradingplatform.api.marketplace.data.entity.User
import io.plexify.tradingplatform.api.marketplace.data.repository.MockUserRepository
import io.plexify.tradingplatform.auth.dto.UserDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: MockUserRepository
) {

    /**
     * Load the user from the ReactiveSecurityContextHolder to load KC-Credentials from the ReactiveOAuth2AuthorizedClientService
     * for Oidc userinfo endpoint access via API call.
     * Out of scope.
     * */
    fun getUserDetails(): Mono<UserDto> {
        return Mono.just(UserDto("Ask keycloak"))
    }

    fun getUser(id: String): Mono<User> {
        return userRepository.findById(id)
    }

    fun createUser(id: String): Mono<User> {
        return userRepository.save(User(id = id))
    }

}