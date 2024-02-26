package io.plexify.tradingplatform.auth.configuration

import io.plexify.tradingplatform.auth.service.UserService
import io.plexify.tradingplatform.config.PathConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val authConfig: AuthConfig,
    private val pathConfig: PathConfig,
    private val userService: UserService
) {
    @Bean
    fun httpSecurity(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .configureAuthorizeExchanges(pathConfig)
            .configureOAuth2Login(authConfig, userService)
            .securityContextRepository(WebSessionServerSecurityContextRepository())
            .configureCsrf()
            .configureCsrfTokenFilter()
            .configureLogout(authConfig)
        return http.build()
    }

    @Bean
    fun clientRegistrationRepository(): ReactiveClientRegistrationRepository {
        return InMemoryReactiveClientRegistrationRepository(keycloakClientRegistration())
    }

    @Bean
    fun corsConfigurationSource(config: AuthConfig): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(config.corsAllowedOrigin)
        configuration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN")
        configuration.setAllowedMethods(listOf("GET", "PUT", "POST", "OPTIONS"))
        configuration.exposedHeaders = listOf("Set-Cookie")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    private fun keycloakClientRegistration(): ClientRegistration {
        val realm = authConfig.realm
        val kcFrontendAddr = authConfig.kcFrontendAddr
        val kcBackendAddr = authConfig.kcBackendAddr
        return ClientRegistration.withRegistrationId("keycloak")
            .clientId(authConfig.kcClientId)
            .clientSecret(authConfig.kcClientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            //kc-backend
            .authorizationUri("$kcBackendAddr/realms/$realm/protocol/openid-connect/auth")
            .tokenUri("$kcBackendAddr/realms/$realm/protocol/openid-connect/token")
            .userInfoUri("$kcBackendAddr/realms/$realm/protocol/openid-connect/userinfo")
            .jwkSetUri("$kcBackendAddr/realms/$realm/protocol/openid-connect/certs")
            .redirectUri("${pathConfig.serverAddr}/trading/{action}/oauth2/code/{registrationId}")
            //kc-frontend
            .authorizationUri("$kcFrontendAddr/realms/$realm/protocol/openid-connect/auth")
            .issuerUri("$kcFrontendAddr/realms/$realm")
            .scope("openid", "offline_access", "profile")
            .clientName("Keycloak")
            .userNameAttributeName("sub")
            .build()
    }

}