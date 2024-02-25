package io.plexify.tradingplatform.api.marketplace.data.repository

import io.plexify.tradingplatform.api.marketplace.data.entity.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.concurrent.ConcurrentHashMap

@Service
class MockUserRepository {
    private val userMap: MutableMap<String, User> = ConcurrentHashMap()

    fun getActiveUser(): Mono<User> {
        return ReactiveSecurityContextHolder.getContext()
            .switchIfEmpty { Mono.error(IllegalStateException("Security Context not found")) }
            .map { ctx ->  (ctx.authentication as OAuth2AuthenticationToken).name}
            .mapNotNull { userMap[it] }
    }

    fun findById(id: String): Mono<User> {
        return Mono.fromCallable {
            val user = userMap[id]
            user
        }
    }

    fun save(user: User): Mono<User>{
        return Mono.fromCallable {
            userMap[user.id] = user
            user
        }
    }
}