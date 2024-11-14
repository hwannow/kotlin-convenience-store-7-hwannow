package store.domain

import store.DiscountState
import store.repository.Repository

class Membership() {
    var membership: DiscountState = DiscountState.NOT_MEMBERSHIP_DISCOUNT
    var membershipDiscountAmount: Int = 0

    fun setValidMembershipDiscountState(input: String) {
        membership = if (input == "Y") DiscountState.MEMBERSHIP_DISCOUNT
        else DiscountState.NOT_MEMBERSHIP_DISCOUNT
    }

    private fun calculateMembershipDiscountAmount(totalPriceWithNoPromotion: Int) {
        membershipDiscountAmount = totalPriceWithNoPromotion * 30 / 100

        if (membershipDiscountAmount > 8000) membershipDiscountAmount = 8000
    }

    fun applyMembershipDiscount(totalPriceWithNoPromotion: Int) {
        if (membership == DiscountState.NOT_MEMBERSHIP_DISCOUNT) return

        calculateMembershipDiscountAmount(totalPriceWithNoPromotion)
    }

    fun resetMembership() {
        membership = DiscountState.NOT_MEMBERSHIP_DISCOUNT
        membershipDiscountAmount = 0
    }
}