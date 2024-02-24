package io.plexify.tradingplatform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository

@SpringBootApplication
class AbstractIntegrationTest {

}

/**
 * Prevent Oidc discovery endpoints from being queried in test context
 * */
@Bean
@Primary
fun clientRegistrationRepository(): ReactiveClientRegistrationRepository {
    return InMemoryReactiveClientRegistrationRepository()
}