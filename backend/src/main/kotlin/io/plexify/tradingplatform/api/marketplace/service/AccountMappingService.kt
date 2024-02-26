package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.MpApiConfig
import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import io.plexify.tradingplatform.api.marketplace.data.entity.Share
import io.plexify.tradingplatform.api.marketplace.data.repository.MockAccountRepository
import io.plexify.tradingplatform.api.marketplace.dto.AccountDto
import io.plexify.tradingplatform.api.marketplace.dto.ShareDto
import io.plexify.tradingplatform.config.PathConfig
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountMappingService(
    private val mockAccountRepository: MockAccountRepository,
    private val sampleDataProvider: SampleDataProvider,
    private val requestContextProvider: RequestContextProvider,
    private val pathConfig: PathConfig
) {

    fun mapActiveAccount(): Mono<AccountDto> {
        return mockAccountRepository.getActiveAccount()
            .flatMap { acc -> accountDto(acc) }
    }

    fun mapAccount(account: Account): Mono<AccountDto> {
        return accountDto(account)
    }

    private fun accountDto(acc: Account): Mono<AccountDto> {
        return mapPortfolio(acc.portfolio)
            .map {
                AccountDto(
                    portfolio = it,
                    acc.balance
                )
            }
    }



    private fun mapPortfolio(portfolio: MutableList<Share>): Mono<List<ShareDto>> {
        return Mono.fromCallable {
            portfolio.map {
                val asset =  sampleDataProvider.sampleData[it.id]
                    ?: throw IllegalStateException("User owns unidentifiable asset!")
                ShareDto(
                    id = it.id,
                    boughtForPrice = it.boughForPrice,
                    label = asset.name,
                    quantity = it.quantity,
                )
            }
        }
            .flatMapIterable { it }
            .flatMap { patchSellUrl(it) }
            .collectList()
    }

    private fun patchSellUrl(share: ShareDto): Mono<ShareDto> {
        return sellUrl(share.id).map {
            share.sellUrl = it
            share
        }
    }

    private fun sellUrl(id: String): Mono<String> {
        return requestContextProvider.serverBaseUrl()
            .map { baseUrl ->
                "$baseUrl${pathConfig.apiBasePath}/${MpApiConfig.ASSET_PATH}/$id/sell"
            }
    }
}