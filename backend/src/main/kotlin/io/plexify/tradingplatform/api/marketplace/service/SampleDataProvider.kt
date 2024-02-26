package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.dto.Asset
import io.plexify.tradingplatform.api.marketplace.service.PriceCalculator.generateStockPrice
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SampleDataProvider {
    val sampleData = createSampleData()

    companion object{

        private fun createSampleData(): HashMap<String, Asset> {
            val res = hashMapOf<String, Asset>()
            for (i in 1..20){
                val id = uuid()
                val name = names[i]!!
                res[id] = Asset(
                    id = id,
                    name = name,
                    prices = mutableListOf(generateStockPrice(name))
                ).apply {
                    availableAssets = (0..10).random()
                }
            }
            return res
        }

        private fun uuid() = UUID.randomUUID().toString()

        private val names = hashMapOf(
            1 to "Hagenbecks Tierpark",
            2 to "Willys Würstenbude",
            3 to "Fantasyland Inc.",
            4 to "Apple",
            5 to "Meta",
            6 to "Tesla",
            7 to "Rheinmetall",
            8 to "Bertas Stullenschmiede",
            9 to "Peterzwegat Inc.",
            10 to "Amazon",
            11 to "Microsoft",
            12 to "Infineon",
            13 to "Dell",
            14 to "Fancy Startup 123",
            15 to "Fanta",
            16 to "Coca Cola Inc.",
            17 to "Nordheide Hospital",
            18 to "Nvidia",
            19 to "Allianz",
            20 to "Biontech",
        )
    }
}