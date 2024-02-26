package io.plexify.tradingplatform.auth.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class AuthConfig(
    @Value("\${cors.allowed.origin}")
    val corsAllowedOrigin: String,
    @Value("\${keycloak.client.id}")
    val kcClientId: String,
    @Value("\${keycloak.client.secret}")
    val kcClientSecret: String,
    @Value("\${auth.failure-url}")
    val authFailureUrl: String,
    @Value("\${auth.success-url}")
    val authSuccessUrl: String,
    @Value("\${auth.keycloak.backend-addr}")
    val kcBackendAddr: String = "",
    @Value("\${auth.keycloak.frontend-addr}")
    val kcFrontendAddr: String = "",
    @Value("\${auth.keycloak.realm}")
    val realm: String = "",
)