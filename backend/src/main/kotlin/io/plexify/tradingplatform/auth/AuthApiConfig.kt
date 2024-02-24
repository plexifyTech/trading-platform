package io.plexify.tradingplatform.auth

import io.plexify.tradingplatform.auth.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter

@Configuration
@EnableWebFlux
class IntegrationsApiConfig {
    @Bean
    fun userApi(userHandler: UserHandler) =
        coRouter {
            AUTH_API_BASE_PATH.nest {
                GET("/user", userHandler::getUser)
                GET("/token", userHandler::getCsrfToken)
            }
        }


    companion object {
        const val AUTH_API_BASE_PATH = "auth"
    }
}