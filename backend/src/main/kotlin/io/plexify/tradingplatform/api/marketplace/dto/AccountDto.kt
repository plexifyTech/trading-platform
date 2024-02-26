package io.plexify.tradingplatform.api.marketplace.dto

data class AccountDto(
    val portfolio: List<ShareDto>,
    var balance: Double
)

data class ShareDto(
    val id: String,
    val boughtForPrice: Double,
    val label: String? = null,
    val quantity: Int
){
    var sellUrl: String? = null
}