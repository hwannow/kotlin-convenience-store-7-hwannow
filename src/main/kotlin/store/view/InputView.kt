package store.view

import camp.nextstep.edu.missionutils.Console
import store.DiscountState
import store.constant.ViewConstant

class InputView {
    private var testInput: String? = null

    fun setTestInput(input: String) {
        testInput = input
    }

    fun readItem(): String {
        println(ViewConstant.INPUT_GUIDE)
        val input = Console.readLine()

        return input
    }

    fun inputConfirmation(condition: DiscountState, name: String, quantity: Int): String {
        when (condition) {
            DiscountState.PROMOTION_ADDITIONAL -> println(String.format(ViewConstant.OUTPUT_PROMOTION_ADDITIONAL_CONFIRMATION, name, quantity))
            DiscountState.PROMOTION_EXCLUSION -> println(String.format(ViewConstant.OUTPUT_PROMOTION_EXCLUSION_CONFIRMATION, name, quantity))
            else -> {}
        }

        return testInput ?: Console.readLine()
    }

    fun inputMembershipState(): String {
        println(ViewConstant.OUTPUT_MEMBERSHIP_DISCOUNT_CONFIRMATION)
        return testInput ?: Console.readLine()
    }

    fun inputAdditionalPurchase(): String {
        println(ViewConstant.OUTPUT_ADDITION_PURCHASE)
        return testInput ?: Console.readLine()
    }
}