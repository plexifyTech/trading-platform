package io.plexify.tradingplatform.api.marketplace.dto

data class AssetResponse(
    val id: String,
    val fields: AssetFields
)

data class AssetFields(
    val details: Asset,
    val buyUrl: String,
    val sellUrl: String
)
