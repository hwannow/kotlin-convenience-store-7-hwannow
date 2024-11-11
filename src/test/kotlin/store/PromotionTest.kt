package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import store.controller.PromotionController
import store.data.Product
import store.data.Promotion
import store.repository.Repository
import store.view.InputView
import java.time.LocalDate

class PromotionTest {
    private val repo = Repository()
    private val inputView = InputView()
    private val productController = PromotionController(repo, inputView)

    @Test
    fun `구매자는 프로모션 할인을 받을 수 있다`() {
        val input = "[콜라-3]"

        repo.customer.getValidPurchases(input)
        productController.updateProduct(repo.customer.purchases[0])

        assertEquals("콜라", repo.promotion.saledItems[0].productName)
        assertEquals(1, repo.promotion.saledItems[0].quantity)
    }
    @Test
    fun `프로모션 할인으로 인해 증정을 추가할 수 있다`() {
        val input = "[콜라-5]"

        inputView.setTestInput("Y")

        repo.customer.getValidPurchases(input)
        productController.updateProduct(repo.customer.purchases[0])

        assertEquals("콜라", repo.promotion.saledItems[0].productName)
        assertEquals(2, repo.promotion.saledItems[0].quantity)
    }

    @Test
    fun `프로모션 재고가 부족할 경우 일부를 혜택없이 결제한다`() {
        val input = "[콜라-14]"

        inputView.setTestInput("Y")

        repo.customer.getValidPurchases(input)
        productController.updateProduct(repo.customer.purchases[0])

        assertEquals("콜라", repo.promotion.saledItems[0].productName)
        assertEquals(3, repo.promotion.saledItems[0].quantity)
    }

    @Test
    fun `프로모션 재고가 부족한 경우 나머지 물품을 결제하지 않을 수 있다`() {
        val input = "[콜라-14]"

        inputView.setTestInput("N")

        repo.customer.getValidPurchases(input)
        productController.updateProduct(repo.customer.purchases[0])

        assertEquals("콜라", repo.promotion.saledItems[0].productName)
        assertEquals(3, repo.promotion.saledItems[0].quantity)
        assertEquals(9, repo.customer.purchases[0].quantity)
    }

    @Test
    fun `프로모션 기간이 아닌 경우 할인을 받을 수 없다`() {
        val invalidPromotion =Promotion("환노우할인", 2, 1, LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"))
        repo.promotion.promotions.add(invalidPromotion)
        val product = Product("환노우", 1000, 10,"환노우할인")
        repo.product.products.add(product)
        val input = "[환노우-4]"

        repo.customer.getValidPurchases(input)
        productController.updateProduct(repo.customer.purchases[0])

        assertTrue(repo.promotion.saledItems.isEmpty())
    }
}