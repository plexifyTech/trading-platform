package io.plexify.tradingplatform.api.marketplace.data.entity

class User(
    val id: String,
    val portfolio: MutableList<Share> = mutableListOf(),
    var balance: Double = 100000.00
) {
    fun findShare(assetId: String): Share? {
        return portfolio.find { it.id == assetId }
    }

    fun addShare(share: Share) {
        portfolio.add(share)
    }
}

data class Share(
    val id: String,
    val price: Double
){
    var quantity: Int = 1
}