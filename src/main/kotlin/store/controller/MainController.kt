package store.controller

import store.data.Purchase
import store.domain.Products
import store.repository.Repository
import store.view.InputView
import store.view.OutputView

class MainController {
    private val inputView = InputView()
    private val outputView = OutputView(Products().products)
    private val repo = Repository()
    private val promotionController = PromotionController(repo, inputView)

    fun run() {
        outputView.printGreeting()
        getValidInput()
        repo.customer.purchases.forEach { purchase ->
            promotionController.applyPromotion(purchase)
        }
        getValidMembership()
        repo.membership.applyMembershipDiscount()
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
                repo.membership.setValidMembershipDiscountState(membership)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}