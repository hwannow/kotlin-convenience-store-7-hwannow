package store.controller

import store.data.Purchase
import store.repository.Repository
import store.view.InputView
import store.view.OutputView

class MainController {
    private val repo = Repository()
    private val inputView = InputView()
    private val promotionController = PromotionController(repo, inputView)
    private val outputView = OutputView(repo.product.products)

    fun run() {
        while (true) {
            outputView.printGreeting()
            getValidInput()
            repo.customer.purchases.forEach { purchase ->
                promotionController.updateProduct(purchase)
            }
            getValidMembership()
            calculatePrice()
            outputView.printReceipt(repo)
            if (getValidAdditionalInput() == "N") break
            repo.reset()
        }
        repo.product.updateInventoryFile()
    }

    private fun calculatePrice() {
        repo.calculateTotalPrice()
        repo.calculateTotalPriceWithNoPromotion()
        repo.calculateTotalPriceWithPromotion()
        repo.membership.applyMembershipDiscount(repo.totalPriceWithNoPromotion)
    }

    private fun validateInput(): MutableList<Purchase> {
        return try {
            val userInput = inputView.readItem()
            repo.customer.getValidPurchases(userInput)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            validateInput()
        }
    }

    private fun getValidInput() {
        while (true) {
            try {
                val purchase = validateInput()
                repo.validateInventory(purchase)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getValidMembership() {
        while (true) {
            try{
                val membership = inputView.inputMembershipState()
                repo.validateConfirmation(membership)
                repo.membership.setValidMembershipDiscountState(membership)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getValidAdditionalInput(): String {
        while (true) {
            try {
                val additionalInput = inputView.inputAdditionalPurchase()
                repo.validateConfirmation(additionalInput)
                return additionalInput
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}