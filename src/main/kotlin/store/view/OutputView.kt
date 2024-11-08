package store.view

import store.constant.ViewConstant
import store.data.Product
import java.text.DecimalFormat

class OutputView(private val products: MutableList<Product>) {
    fun printGreeting() {
        println(ViewConstant.OUTPUT_GREETINGS)
        val dec = DecimalFormat("#,###")
        products.forEach { product ->
            val quantityDisplay = product.quantity.takeIf { it > 0 }?.toString() ?: "재고없음"
            val promotionDisplay = product.promotion.takeUnless { it.equals("null") } ?: ""
            println(ViewConstant.OUTPUT_INVENTORY.format(
                product.name,
                dec.format(product.price),
                quantityDisplay,
                promotionDisplay
            ))
        }
    }
}