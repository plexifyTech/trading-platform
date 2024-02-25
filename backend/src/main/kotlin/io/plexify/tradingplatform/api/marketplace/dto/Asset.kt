package io.plexify.tradingplatform.api.marketplace.dto

data class Asset(
    val id: String,
    val name: String,
    val prices: List<Double> = mutableListOf()
){
    var availableAssets: Int = 0
}
