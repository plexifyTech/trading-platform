package io.plexify.tradingplatform.api


import io.plexify.tradingplatform.api.marketplace.handler.AssetHandler
import io.plexify.tradingplatform.api.marketplace.handler.PublicAssetHandler
import io.plexify.tradingplatform.config.PathConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router


@Configuration
@EnableWebFlux
class MpApiConfig(
    private val pathConfig: PathConfig
) {
    @Bean
    fun sseRouter(assetHandler: PublicAssetHandler) =
        router {
            accept(MediaType.TEXT_EVENT_STREAM)
                .nest {
                    GET("${pathConfig.apiPublicPath}/$ASSET_PATH/stream", assetHandler::subscribeToPriceUpdates)
                }
        }

    @Bean
    fun assetRouter(publicHandler: PublicAssetHandler, assetHandler: AssetHandler) = coRouter {
        accept(MediaType.APPLICATION_JSON)
            .nest {
                pathConfig.apiPublicPath.nest {
                    GET("/$ASSET_PATH", publicHandler::listAssets)
                }
                pathConfig.apiBasePath.nest {
                    GET("/$ASSET_PATH/portfolio", assetHandler::getPortfolio)
                    PUT("/$ASSET_PATH/{id}/buy", assetHandler::buyAsset)
                }
            }
    }


    companion object {
        const val ASSET_PATH = "assets"
    }
}