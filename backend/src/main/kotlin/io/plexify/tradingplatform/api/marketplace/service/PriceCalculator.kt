package io.plexify.tradingplatform.api.marketplace.service

import kotlin.math.abs
import kotlin.math.sin

object PriceCalculator {
    fun generateStockPrice(name: String): Double {
        val hash = name.chars().reduce(0) { a: Int, b: Int -> (a shl 5) - a + b }
        val amplitude = 100
        val n = System.currentTimeMillis()
        val regularPrice = abs(3 * sin((2 * hash * n).toDouble())) % amplitude
        //inflation
        return regularPrice * 5000
    }
}
