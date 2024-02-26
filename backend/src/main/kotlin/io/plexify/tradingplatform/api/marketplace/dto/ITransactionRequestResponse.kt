package io.plexify.tradingplatform.api.marketplace.dto

data class TransactionResponse(
    val success: Boolean,
    val message: String,
    val account: AccountDto? = null
)


