package io.plexify.tradingplatform.api.marketplace.service

import io.plexify.tradingplatform.api.marketplace.data.entity.Share
import io.plexify.tradingplatform.api.marketplace.data.entity.User
import io.plexify.tradingplatform.api.marketplace.data.repository.MockUserRepository
import io.plexify.tradingplatform.api.marketplace.dto.Asset
import io.plexify.tradingplatform.api.marketplace.dto.BuyRequest
import io.plexify.tradingplatform.api.marketplace.dto.BuyRequestResponse
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class AssetTransactionService(
    private val userRepository: MockUserRepository,
    private val sampleDataProvider: SampleDataProvider,
) {
    fun handleBuyRequest(assetId: String, buyRequest: BuyRequest): Mono<BuyRequestResponse> {
        val price = buyRequest.price
        val asset = sampleDataProvider.sampleData[assetId]
            ?: throw IllegalStateException("Can't find the asset you wanted to buy. Aborted transaction.")
        return userRepository.getActiveUser()
            .flatMap { validateUserRequest(it, asset, price) }
            .flatMap { validBuyer -> performTransaction(validBuyer, asset, price) }
            .onErrorResume { ex -> BuyRequestResponse(success = false, message = ex.localizedMessage).toMono() }
    }

    private fun validateUserRequest(user: User, asset: Asset, price: Double): Mono<User> {
        return Mono.fromCallable {
            val isLiquid = user.balance - price > 0.00
            Assert.isTrue(
                isLiquid,
                "Your total balance does not cover the price of the requested asset. Aborted transaction."
            )
            val isAvailable = asset.availableAssets > 0
            Assert.isTrue(isAvailable, "The asset you requested is currently out of unavailable. Aborted transaction.")
            user
        }

    }

    private fun performTransaction(user: User, asset: Asset, price: Double): Mono<BuyRequestResponse> {
        val existingAsset = user.findShare(asset.id)
        val pub = if (existingAsset != null) {
            addShare(existingAsset, user, price)
        } else {
            purchase(user, asset, price)
        }
        return pub
            .map {
                removeFromMarket(asset)
                BuyRequestResponse(
                    success = true,
                    message = "Successfully added a ${asset.name} share to your portfolio."
                )
            }
    }

    private fun addShare(
        existingAsset: Share,
        user: User,
        price: Double
    ): Mono<User> {
        existingAsset.quantity++
        user.balance -= price
        return userRepository.save(user)
    }

    private fun purchase(user: User, asset: Asset, price: Double): Mono<User> {
        val newShare = Share(id = asset.id, price = price)
        user.addShare(newShare)
        user.balance -= price
        return userRepository.save(user)
    }

    private fun removeFromMarket(asset: Asset) {
        asset.availableAssets--
        sampleDataProvider.sampleData[asset.id] = asset
    }
}