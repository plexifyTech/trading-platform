package io.plexify.tradingplatform.api


import io.plexify.tradingplatform.api.marketplace.handler.AssetHandler
import io.plexify.tradingplatform.config.PathConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.router


@Configuration
@EnableWebFlux
class MpApiConfig(
    private val pathConfig: PathConfig
) {
    @Bean
    fun router(assetHandler: AssetHandler) =
        router {
            accept(MediaType.TEXT_EVENT_STREAM)
                .nest {
                    GET("${pathConfig.apiPublicPath}/$ASSET_PATH/stream", assetHandler::subscribeToPriceUpdates)
                }
        }


    companion object {
        const val ASSET_PATH = "assets"
    }
}