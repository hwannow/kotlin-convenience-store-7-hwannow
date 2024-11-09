package store.controller

import store.DiscountState
import store.data.Product
import store.data.Promotion
import store.data.Purchase
import store.repository.Repository
import store.view.InputView
import camp.nextstep.edu.missionutils.DateTimes
import java.time.LocalDate

class PromotionController(
    private val repo: Repository,
    private val inputView: InputView
) {
    fun applyPromotion(purchase: Purchase) {
        val product = repo.product.getProductToBuy(purchase) ?: return
        val promotion = repo.promotion.getPromotionToBuy(product)
        val condition = getDiscountState(purchase, product, promotion)
        if (!isCurrentDateInPromotionWindow(promotion.startDate, promotion.endDate)) {
            return
        }
        when (condition) {
            DiscountState.PROMOTION_APPLICABLE -> handlePromotionApplication(promotion, purchase)
            DiscountState.PROMOTION_EXCLUSION -> handlePromotionExclusion(product, promotion, purchase)
            DiscountState.PROMOTION_ADDITIONAL -> handleAdditionalPromotion(promotion, purchase)
            else -> {}
        }
    }

    // 프로모션 적용이 깔끔하게 완료된 경우 로직
    private fun handlePromotionApplication(promotion: Promotion, purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get

        repo.promotion.addSaledItem(Purchase(
            purchase.productName, purchase.quantity / buyPlusGet
        ))
    }

    // 프로모션에 의해 물품을 추가로 증정할 수 있는 경우
    private fun handleAdditionalPromotion(promotion: Promotion, purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get
        val additionalItem = if (getValidConfirm(DiscountState.PROMOTION_ADDITIONAL, purchase, promotion) == "Y") {
            promotion.get
        } else {
            0
        }

        repo.promotion.addSaledItem(Purchase(
            purchase.productName, purchase.quantity / buyPlusGet + additionalItem
        ))
    }

    // 프로모션 재고가 부족하여 프로모션을 적용하지 못하는 경우
    private fun handlePromotionExclusion(product: Product, promotion: Promotion, purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get
        val maxQuantity = (product.quantity / buyPlusGet) * buyPlusGet

        val answer = getValidConfirm(DiscountState.PROMOTION_EXCLUSION, purchase, promotion)
        if (answer == "N") {
            purchase.quantity -= purchase.quantity - maxQuantity
        }

        repo.promotion.addSaledItem(Purchase(
            purchase.productName, product.quantity / buyPlusGet
        ))
    }

    private fun getDiscountState(purchase: Purchase, productToBuy: Product, promotionToBuy: Promotion): DiscountState {
        val buyPlusGet = promotionToBuy.buy + promotionToBuy.get
        return when {
            purchase.quantity > productToBuy.quantity -> DiscountState.PROMOTION_EXCLUSION
            purchase.quantity == productToBuy.quantity && purchase.quantity % buyPlusGet != 0 -> DiscountState.PROMOTION_EXCLUSION
            purchase.quantity % buyPlusGet == promotionToBuy.buy && purchase.quantity + promotionToBuy.get <= productToBuy.quantity -> DiscountState.PROMOTION_ADDITIONAL
            else -> DiscountState.PROMOTION_APPLICABLE
        }
    }

    private fun getValidConfirm(condition: DiscountState, purchase: Purchase, promotion: Promotion): String {
        var answer: String = ""
        while (true) {
            try {
                answer = inputView.inputConfirmation(condition, purchase.productName, promotion.get)
                repo.validateConfirmation(answer)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
        return answer
    }

    private fun getCurrentDate(): LocalDate {
        val currentTime = DateTimes.now()
        return currentTime.toLocalDate()
    }

    private fun isCurrentDateInPromotionWindow(startDate: LocalDate, endDate: LocalDate): Boolean {
        val currentTime = getCurrentDate()
        return currentTime in startDate..endDate
    }
}