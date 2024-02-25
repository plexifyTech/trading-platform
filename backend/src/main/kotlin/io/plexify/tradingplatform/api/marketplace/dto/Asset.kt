package io.plexify.tradingplatform.api.marketplace.dto

data class Asset(
    val id: String,
    val name: String,
    val availableAssets: Int,
    val price: List<Double> = mutableListOf()
)
