package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.data.entity.Share
import io.plexify.tradingplatform.api.marketplace.data.entity.Account
import io.plexify.tradingplatform.api.marketplace.data.repository.MockAccountRepository
import io.plexify.tradingplatform.api.marketplace.dto.*
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.lang.IllegalArgumentException

@Service
class AssetTransactionService(
    private val userRepository: MockAccountRepository,
    private val sampleDataProvider: SampleDataProvider,
    private val accountMappingService: AccountMappingService
) {
    fun handleBuyRequest(assetId: String, buyRequest: TransactionRequest): Mono<TransactionResponse> {
        val price = buyRequest.price
        val asset = sampleDataProvider.sampleData[assetId]
            ?: throw IllegalStateException("Can't find the asset you wanted to buy. Aborted transaction.")
        return userRepository.getActiveAccount()
            .flatMap { account -> validateUserRequest(account, asset, price) }
            .flatMap { validBuyer -> performTransaction(validBuyer, asset, price) }
            .onErrorResume { ex -> TransactionResponse(success = false, message = ex.localizedMessage).toMono() }
    }

    fun handleSellRequest(assetId: String): Mono<TransactionResponse> {
        val asset = sampleDataProvider.sampleData[assetId]
            ?: throw IllegalStateException("Can't find the asset you wanted to sell. Aborted transaction.")
        return userRepository.getActiveAccount()
            .flatMap { account -> sellAsset(account, asset) }
            .onErrorResume { ex -> TransactionResponse(success = false, message = ex.localizedMessage).toMono() }
    }

    private fun sellAsset(account: Account, asset: Asset): Mono<TransactionResponse> {
        val share = account.findShare(asset.id) ?: throw IllegalArgumentException("You don't own this asset!")
        val currentPrice = asset.prices.last()
        addToMarket(asset, share.quantity)
        return sell(account, share, currentPrice, asset)
            .flatMap { accountMappingService.mapAccount(account) }
            .map { acc ->
                TransactionResponse(
                    success = true,
                    message = "Successfully sold Share of ${asset.name}. Quantity: ${share.quantity}",
                    account = acc
                )
            }
    }

    private fun sell(
        account: Account,
        share: Share,
        price: Double,
        asset: Asset
    ): Mono<Account> {
        account.balance += (share.quantity * price)
        account.removeAsset(asset.id)
        return userRepository.save(account)
    }

    private fun validateUserRequest(account: Account, asset: Asset, price: Double): Mono<Account> {
        return Mono.fromCallable {
            val isLiquid = account.balance - price > 0.00
            Assert.isTrue(
                isLiquid,
                "Your total balance does not cover the price of the requested asset. Aborted transaction."
            )
            val isAvailable = asset.availableAssets > 0
            Assert.isTrue(isAvailable, "The asset you requested is currently out of unavailable. Aborted transaction.")
            account
        }

    }

    private fun performTransaction(account: Account, asset: Asset, price: Double): Mono<TransactionResponse> {
        val existingAsset = account.findShare(asset.id)
        val pub = if (existingAsset != null) {
            addShare(existingAsset, account, price)
        } else {
            purchase(account, asset, price)
        }
        return pub
            .flatMap {
                removeFromMarket(asset)
                accountMappingService.mapAccount(account)
            }.map { acc ->
                TransactionResponse(
                    success = true,
                    message = "Successfully added a ${asset.name} share to your portfolio.",
                    account = acc
                )
            }
    }

    private fun addShare(
        existingAsset: Share,
        account: Account,
        price: Double
    ): Mono<Account> {
        existingAsset.quantity++
        account.balance -= price
        return userRepository.save(account)
    }

    private fun purchase(account: Account, asset: Asset, price: Double): Mono<Account> {
        val newShare = Share(id = asset.id, boughForPrice = price)
        account.addShare(newShare)
        account.balance -= price
        return userRepository.save(account)
    }

    private fun removeFromMarket(asset: Asset) {
        asset.availableAssets--
        sampleDataProvider.sampleData[asset.id] = asset
    }

    private fun addToMarket(asset: Asset, quantity: Int) {
        asset.availableAssets += quantity
        sampleDataProvider.sampleData[asset.id] = asset
    }

}