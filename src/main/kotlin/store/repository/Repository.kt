package store.repository

import store.constant.ErrorConstant
import store.data.Product
import store.data.Promotion
import store.data.Purchase
import store.domain.Customer
import store.domain.Membership
import store.domain.Products
import store.domain.Promotions

class Repository {
    val product = Products()
    val promotion = Promotions()
    val customer = Customer()
    val membership = Membership()

    var totalPrice: Int = 0
    var totalPriceWithPromotion: Int = 0
    var totalPriceWithNoPromotion: Int = 0

    fun reset() {
        totalPrice = 0
        totalPriceWithPromotion = 0
        totalPriceWithNoPromotion = 0
        promotion.resetPromotion()
        membership.resetMembership()
        customer.resetCustomer()
    }

    fun validateInventory(purchases: MutableList<Purchase>) {
        purchases.forEach { purchase ->
            val productNoPromotion = product.products.find { it.name == purchase.productName && it.promotion == "null" }
            val productYesPromotion = product.products.find { it.name == purchase.productName && it.promotion != "null" }
            if (productNoPromotion == null && productYesPromotion == null)
                throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_NON_EXISTENT_PRODUCT)

            var maxQuantity: Int = 0
            if (productNoPromotion != null) maxQuantity += productNoPromotion.quantity
            if (productYesPromotion != null) maxQuantity += productYesPromotion.quantity

            if (maxQuantity < purchase.quantity)
                throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_PURCHASE_EXCEEDS_STOCK)
        }
    }

    fun validateConfirmation(input: String) {
        if (input != "Y" && input != "N")
            throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_CONFIRMATION)
    }

    fun updateProductWithPromotion(purchase: Purchase, productWithPromotion: Product, promotionToBuy: Promotion) {
        val buyPlusGet: Int = promotionToBuy.get + promotionToBuy.buy
        productWithPromotion.quantity -= purchase.quantity

        if (productWithPromotion.quantity >= buyPlusGet) return

        val productWithNoPromotion = product.getProductToBuyWithNoPromotion(purchase.productName)
            ?: return

        productWithNoPromotion.quantity += productWithPromotion.quantity
        productWithPromotion.quantity = 0
    }

    fun updateProductWithNoPromotion(purchase: Purchase, productWithNoPromotion: Product) {
        productWithNoPromotion.quantity -= purchase.quantity
    }

    fun calculateTotalPrice() {
        customer.purchases.forEach { purchase ->
            val productToBuy = product.products.find { it.name == purchase.productName }!!
            totalPrice += productToBuy.price * purchase.quantity
        }
    }

    // 멤버십 할인을 계산하기 위함
    fun calculateTotalPriceWithNoPromotion() {
        customer.purchases.forEach{ purchase ->
            val checkPromotion = promotion.saledItems.find { it.productName == purchase.productName }
            val productToBuy =  product.products.find { it.name == purchase.productName }!!
            if (checkPromotion == null) totalPriceWithNoPromotion += productToBuy.price * purchase.quantity
        }
    }

    fun calculateTotalPriceWithPromotion() {
        promotion.saledItems.forEach { purchase ->
            val productToBuy = product.getProductToBuyWithPromotion(purchase.productName)!!
            totalPriceWithPromotion += productToBuy.price * purchase.quantity
        }
    }
}