package store.view

import store.constant.ViewConstant
import store.data.Product
import store.data.Purchase
import store.domain.Products
import store.domain.Promotions
import store.repository.Repository
import java.text.DecimalFormat
import javax.swing.text.View

class OutputView(private val products: MutableList<Product>) {
    val dec = DecimalFormat("#,###")
    fun printGreeting() {
        println(ViewConstant.OUTPUT_GREETINGS)
        products.forEach { product ->
            val quantityDisplay = product.quantity.takeIf { it > 0 }?.let { "${it}개" } ?: "재고 없음"
            val promotionDisplay = product.promotion.takeUnless { it.equals("null") } ?: ""
            println(ViewConstant.OUTPUT_INVENTORY.format(
                product.name,
                dec.format(product.price),
                quantityDisplay,
                promotionDisplay
            ))
        }
    }

    fun printReceipt(repo: Repository) {
        println(ViewConstant.OUTPUT_HEADER_TITLE)
        printPurchase(repo)

        println(ViewConstant.OUTPUT_HEADER_GIFT)
        printPromotionPurchase(repo)

        println(ViewConstant.OUTPUT_HEADER_RESULT)
        printResult(repo)
    }

    private fun printPurchase(repo: Repository) {
        repo.customer.purchases.forEach { purchase ->
            val productToBuy = repo.product.products.find { it.name == purchase.productName }!!
            println(String.format(
                    ViewConstant.OUTPUT_PURCHASE,
                    purchase.productName,
                    purchase.quantity,
                    dec.format(productToBuy.price)
                )
            )
        }
    }

    private fun printPromotionPurchase(repo: Repository) {
        repo.promotion.saledItems.forEach { item ->
            println(String.format(ViewConstant.OUTPUT_GIFT, item.productName, item.quantity))
        }
    }

    private fun printResult(repo: Repository) {
        println(String.format(ViewConstant.OUTPUT_PURCHASE, "총구매액", repo.customer.purchases.sumOf { it.quantity }, dec.format(repo.totalPrice)))
        println(String.format(ViewConstant.OUTPUT_PROMOTION_DISCOUNT, dec.format(repo.totalPriceWithPromotion)))
        println(String.format(ViewConstant.OUTPUT_MEMBERSHIP_DISCOUNT, dec.format(repo.membership.membershipDiscountAmount)))
        println(String.format(ViewConstant.OUTPUT_PAY_MONEY, dec.format(repo.totalPrice - repo.totalPriceWithPromotion - repo.membership.membershipDiscountAmount)))
    }
}