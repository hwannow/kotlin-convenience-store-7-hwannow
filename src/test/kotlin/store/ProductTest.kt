package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.controller.PromotionController
import store.data.Product
import store.domain.Customer
import store.domain.Products
import store.domain.Promotions
import store.repository.Repository
import store.view.InputView

class ProductTest {
    private val repo = Repository()

    private val productName = repo.product.products[0].name
    private val productQuantity = repo.product.products[0].quantity

    private val inputView = InputView()

    private val promotionController = PromotionController(repo, inputView)

    @Test
    fun `고객이 상품을 구입하면 재고에서 차감된다`() {
        repo.customer.getValidPurchases("[$productName-${productQuantity - 2}]")
        inputView.setTestInput("Y")
        repo.customer.purchases.forEach { purchase ->
            promotionController.updateProduct(purchase)
        }

        repo.product.updateInventoryFile()
        val products = Products()
        assertEquals(products.products, repo.product.products)
    }

    @Test
    fun `존재하지 않는 상품을 입력한 경우 에러가 발생한다`() {
        repo.customer.getValidPurchases("[이런과자없는데요-1]")

        assertThrows<IllegalArgumentException> {
            repo.validateInventory(repo.customer.purchases)
        }
    }

    @Test
    fun `구매 수량이 재고 수량을 초과한 경우 에러가 발생한다`() {
        repo.customer.getValidPurchases("[$productName-${productQuantity + 100}]")

        assertThrows<IllegalArgumentException> {
            repo.validateInventory(repo.customer.purchases)
        }
    }
}