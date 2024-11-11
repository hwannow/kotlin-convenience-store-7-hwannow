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
    var product: Product = Product("", 0, 0, "null")
    private lateinit var promotion: Promotion

    fun updateProduct(purchase: Purchase) {
        product = Product("", 0, 0, "null")
        getProductAndPromotionToBuy(purchase)
        if (product.name == "") {
            handleNoPromotion(purchase)
            return
        }
        val condition = getDiscountState(purchase)
        handlePromotionWithCondition(condition, purchase)
        repo.updateProductWithPromotion(purchase, product, promotion)
    }

    private fun handlePromotionWithCondition(condition: DiscountState, purchase: Purchase) {
        when (condition) {
            DiscountState.PROMOTION_APPLICABLE -> handlePromotionApplication(purchase)
            DiscountState.PROMOTION_EXCLUSION -> handlePromotionExclusion(purchase)
            DiscountState.PROMOTION_ADDITIONAL -> handleAdditionalPromotion(purchase)
            DiscountState.PROMOTION_NOT_APPLICABLE -> handleNoPromotion(purchase)
            else -> {}
        }
    }

    private fun handleNoPromotion(purchase: Purchase) {
        product = repo.product.getProductToBuyWithNoPromotion(purchase.productName)!!
        repo.updateProductWithNoPromotion(purchase, product)
    }

    // 프로모션 적용이 깔끔하게 완료된 경우 로직
    private fun handlePromotionApplication(purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get
        repo.promotion.addSaledItem(Purchase(purchase.productName, purchase.quantity / buyPlusGet))
    }

    // 프로모션에 의해 물품을 추가로 증정할 수 있는 경우
    private fun handleAdditionalPromotion(purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get
        val additionalItem = if (getValidConfirm(DiscountState.PROMOTION_ADDITIONAL, purchase.productName, promotion.get) == "Y") {
            promotion.get
        } else {
            0
        }
        purchase.quantity += additionalItem
        repo.promotion.addSaledItem(Purchase(purchase.productName, purchase.quantity / buyPlusGet))
    }

    // 프로모션 재고가 부족하여 프로모션을 적용하지 못하는 경우
    private fun handlePromotionExclusion(purchase: Purchase) {
        val buyPlusGet = promotion.buy + promotion.get
        val maxQuantity = (product.quantity / buyPlusGet) * buyPlusGet

        val answer = getValidConfirm(DiscountState.PROMOTION_EXCLUSION, purchase.productName, purchase.quantity - maxQuantity)
        if (answer == "N") {
            purchase.quantity -= purchase.quantity - maxQuantity
        }
        repo.promotion.addSaledItem(Purchase(purchase.productName, product.quantity / buyPlusGet))
    }

    private fun getProductAndPromotionToBuy(purchase: Purchase) {
        product = repo.product.getProductToBuyWithPromotion(purchase) ?: return
        promotion = repo.promotion.getPromotionToBuy(product)
    }

    private fun getDiscountState(purchase: Purchase): DiscountState {
        val buyPlusGet = promotion.buy + promotion.get
        return when {
            !isCurrentDateInPromotionWindow(promotion.startDate, promotion.endDate) -> DiscountState.PROMOTION_NOT_APPLICABLE
            product.quantity == 0 -> DiscountState.PROMOTION_NOT_APPLICABLE
            purchase.quantity > product.quantity -> DiscountState.PROMOTION_EXCLUSION
            purchase.quantity == product.quantity && purchase.quantity % buyPlusGet != 0 -> DiscountState.PROMOTION_EXCLUSION
            purchase.quantity % buyPlusGet == promotion.buy && purchase.quantity + promotion.get <= product.quantity -> DiscountState.PROMOTION_ADDITIONAL
            else -> DiscountState.PROMOTION_APPLICABLE
        }
    }

    private fun getValidConfirm(condition: DiscountState, name: String, quantity: Int): String {
        var answer: String = ""
        while (true) {
            try {
                answer = inputView.inputConfirmation(condition, name, quantity)
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