package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.domain.Customer

class CustomerTest {
    private val customer: Customer = Customer()

    @Test
    fun `구매자는 상품명과 수량을 입력해 상품을 구매할 수 있다`() {
        val correctInput = "[콜라-3],[에너지바-5]"

        val purchases = customer.getValidPurchases(correctInput)
        assertEquals(2, purchases.size)
        assertEquals("콜라", purchases[0].productName)
        assertEquals(3, purchases[0].quantity)
        assertEquals("에너지바", purchases[1].productName)
        assertEquals(5, purchases[1].quantity)
    }

    @Test
    fun `올바르지 않은 형식으로 입력하면 에러가 발생한다`() {
        val wrongInput = "콜라 5개 주세요."

        assertThrows<IllegalArgumentException> {
            customer.getValidPurchases(wrongInput)
        }
    }
}