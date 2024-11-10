package store.domain

import store.constant.ErrorConstant
import store.data.Purchase

class Customer {
    var purchases: MutableList<Purchase> = mutableListOf()
    var totalPriceWithNoPromotion: Int = 0

    fun getValidPurchases(input: String): MutableList<Purchase> {
        purchases = mutableListOf()
        inputFormatValidation(input)
        parsingInput(input)
        return purchases
    }

    private fun parsingInput(input: String): MutableList<Purchase> {
        val items = input.split("],[", "[", "]").filter { it.isNotEmpty() }
        items.forEach { item ->
            val parts = item.split("-")
            val product = Purchase(
                parts[0],
                parts[1].toIntOrNull() ?: throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_QUANTITY_NOT_INTEGER_MESSAGE)
            )
            if (product.quantity == 0) throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_QUANTITY_IS_ZERO)
            purchases.add(product)
        }
        return purchases
    }

    private fun inputFormatValidation(input: String) {
        if (!input.matches(Regex("""\[[가-힣]+-\d+](,\[[가-힣]+-\d+])*"""))) {
            throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_FORMAT_MESSAGE)
        }
    }
}