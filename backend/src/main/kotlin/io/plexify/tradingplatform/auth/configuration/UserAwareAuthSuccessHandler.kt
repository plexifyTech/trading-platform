package io.plexify.tradingplatform.auth.configuration

import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import io.plexify.tradingplatform.auth.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

class UserAwareAuthSuccessHandler(
    redirectUrl: String,
    private val userService: UserService
): RedirectServerAuthenticationSuccessHandler(redirectUrl) {

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        println("SUCCESS!!")
        return createUserIfAbsent(authentication)
            .flatMap {
                super.onAuthenticationSuccess(webFilterExchange, authentication)
            }
    }

    private fun createUserIfAbsent(authentication: Authentication): Mono<Account> {
        return userService.getUser(authentication.name)
            .switchIfEmpty { userService.createUser(authentication.name) }
    }

}