package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import store.controller.PromotionController
import store.repository.Repository
import store.view.InputView

class MembershipTest {
    private val repo = Repository()
    private val inputView = InputView()
    private val promotionController = PromotionController(repo, inputView)
    @Test
    fun `멤버십 회원은 멤버십 할인을 받을 수 있다`() {
        repo.membership.setValidMembershipDiscountState("Y", repo)

        assertEquals(DiscountState.MEMBERSHIP_DISCOUNT, repo.membership)
    }

    @Test
    fun `프로모션 미적용 금액의 30%를 할인받는다`() {
        val input = "[콜라-3],[에너지바-5]"

        repo.customer.getValidPurchases(input)
        repo.customer.purchases.forEach { purchase ->
            promotionController.applyPromotion(purchase)
        }
        inputView.setTestInput("Y")

        repo.customer.calculateTotalPriceWithNoPromotion(repo.promotion, repo.product)
        repo.membership.calculateMembershipDiscountAmount(repo.customer.totalPriceWithNoPromotion)

        assertEquals(3000, repo.membership.membershipDiscountAmount)
    }

    @Test
    fun `멤버십 할인의 최대 한도는 8,000원이다`() {
        val input = "[물-10],[비타민워터-6],[에너지바-5],[정식도시락-8]"

        repo.customer.getValidPurchases(input)
        repo.customer.purchases.forEach { purchase ->
            promotionController.applyPromotion(purchase)
        }
        inputView.setTestInput("Y")

        repo.customer.calculateTotalPriceWithNoPromotion(repo.promotion, repo.product)
        repo.membership.calculateMembershipDiscountAmount(repo.customer.totalPriceWithNoPromotion)

        assertEquals(8000, repo.membership.membershipDiscountAmount)
    }
}
