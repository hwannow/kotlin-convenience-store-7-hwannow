package store.repository

import store.constant.ErrorConstant
import store.data.Purchase
import store.domain.Customer
import store.domain.Membership
import store.domain.Products
import store.domain.Promotions

class Repository {
    val product = Products()
    val promotion = Promotions()
    val customer = Customer()
    val membership = Membership(this)

    fun validateInventory(purchases: MutableList<Purchase>) {
        purchases.forEach { purchase ->
            val productNoPromotion =
                product.products.find { it.name == purchase.productName && it.promotion == "null" }
            val productYesPromotion =
                product.products.find { it.name == purchase.productName && it.promotion != "null" }
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
}