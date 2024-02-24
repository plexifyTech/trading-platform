package io.plexify.tradingplatform.auth.configuration

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

class KeycloakLogoutHandler : RedirectServerLogoutSuccessHandler() {
    override fun onLogoutSuccess(
        exchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        val oicdUser = authentication.principal as OidcUser
        return logoutFromKeycloak(oicdUser).flatMap {
            super.onLogoutSuccess(exchange, authentication)
        }
    }

    private fun logoutFromKeycloak(user: OidcUser): Mono<ResponseEntity<Void>> {
        val endSessionEndpoint = user.issuer.toString() + "/protocol/openid-connect/logout"
        val uri =
            UriComponentsBuilder
                .fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", user.idToken.tokenValue)
                .toUriString()
        return WebClient.create()
            .get()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
    }
}