package io.plexify.tradingplatform.api.marketplace.data.repository

import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.concurrent.ConcurrentHashMap

@Service
class MockAccountRepository {
    private val accountMap: MutableMap<String, Account> = ConcurrentHashMap()

    fun getActiveAccount(): Mono<Account> {
        return ReactiveSecurityContextHolder.getContext()
            .switchIfEmpty { Mono.error(IllegalStateException("Security Context not found")) }
            .map { ctx ->  (ctx.authentication as OAuth2AuthenticationToken).name}
            .mapNotNull { accountMap[it] }
    }

    fun findById(id: String): Mono<Account> {
        return Mono.fromCallable {
            val user = accountMap[id]
            user
        }
    }

    fun save(account: Account): Mono<Account>{
        return Mono.fromCallable {
            accountMap[account.id] = account
            account
        }
    }
}