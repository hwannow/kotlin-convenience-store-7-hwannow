package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.domain.Customer
import store.domain.Products

class ProductTest {
    private val product = Products()
    private val customer = Customer()

    private val productName = product.products[0].name
    private val productQuantity =product.products[0].quantity

    @Test
    fun `고객이 상품을 구입하면 재고에서 차감된다`() {
        customer.buy("[$productName-${productQuantity - 2}]")

        product.deductFromInventory(customer.purchases)

        assertEquals(2, product.products[0].quantity)
    }

    @Test
    fun `존재하지 않는 상품을 입력한 경우 에러가 발생한다`() {
        customer.buy("[이런과자없는데요-1]")

        assertThrows<IllegalArgumentException> {
            product.deductFromInventory(customer.purchases)
        }
    }

    @Test
    fun `구매 수량이 재고 수량을 초과한 경우 에러가 발생한다`() {
        customer.buy("[$productName-${productQuantity + 2}]")

        assertThrows<IllegalArgumentException> {
            product.deductFromInventory(customer.purchases)
        }
    }
}