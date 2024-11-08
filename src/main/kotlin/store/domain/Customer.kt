package store.domain

import store.constant.ErrorConstant
import store.data.Purchase

class Customer {
    var purchases: MutableList<Purchase> = mutableListOf()

    fun buy(input: String): MutableList<Purchase> {
        inputFormatValidation(input)
        parsingInput(input)
        return purchases
    }

    private fun parsingInput(input: String): MutableList<Purchase> {
        inputFormatValidation(input)
        val items = input.split("],[", "[", "]").filter { it.isNotEmpty() }
        items.forEach { item ->
            val parts = item.split("-")
            val product = Purchase(
                parts[0],
                parts[1].toIntOrNull() ?: throw IllegalArgumentException(ErrorConstant.ERROR_INPUT_QUANTITY_NOT_INTEGER_MESSAGE)
            )
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