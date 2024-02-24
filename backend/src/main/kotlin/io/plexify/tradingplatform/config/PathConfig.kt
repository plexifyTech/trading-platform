package io.plexify.tradingplatform.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class PathConfig(
    @Value("\${api.base-path}")
    val apiBasePath: String,
    @Value("\${api.public-base-path}")
    val apiPublicPath: String,
)
