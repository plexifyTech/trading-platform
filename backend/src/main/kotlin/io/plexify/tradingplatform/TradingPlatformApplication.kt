package io.plexify.tradingplatform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TradingPlatformApplication

fun main(args: Array<String>) {
	runApplication<TradingPlatformApplication>(*args)
}
