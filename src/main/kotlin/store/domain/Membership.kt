package store.domain

import store.DiscountState
import store.repository.Repository

class Membership(private val repo: Repository) {
    private var membership: DiscountState = DiscountState.NOT_MEMBERSHIP_DISCOUNT
    private var totalPriceWithNoPromotion: Int = 0
    var membershipDiscountAmount: Int = 0

    fun setValidMembershipDiscountState(input: String) {
        repo.validateConfirmation(input)
        membership = if (input == "Y") DiscountState.MEMBERSHIP_DISCOUNT
        else DiscountState.NOT_MEMBERSHIP_DISCOUNT
    }

    private fun calculateTotalPriceWithNoPromotion(promotion: Promotions, product: Products) {
        repo.customer.purchases.forEach{ purchase ->
            val checkPromotion = promotion.saledItems.find { it.productName == purchase.productName }
            val productToBuy =  product.products.find { it.name == purchase.productName }!!
            if (checkPromotion == null) totalPriceWithNoPromotion += productToBuy.price * purchase.quantity
        }
    }

    private fun calculateMembershipDiscountAmount() {
        membershipDiscountAmount = totalPriceWithNoPromotion * 30 / 100

        if (membershipDiscountAmount > 8000) membershipDiscountAmount = 8000
    }

    fun applyMembershipDiscount() {
        if (membership == DiscountState.NOT_MEMBERSHIP_DISCOUNT) return

        calculateTotalPriceWithNoPromotion(repo.promotion, repo.product)
        calculateMembershipDiscountAmount()
    }
}