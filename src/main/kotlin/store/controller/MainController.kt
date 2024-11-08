package store.controller

import store.data.Purchase
import store.domain.Customer
import store.domain.Products
import store.domain.Promotions
import store.view.InputView
import store.view.OutputView

class MainController {
    private val inputView = InputView()
    private val outputView = OutputView(Products().products)
    private val customer = Customer()
    private val product = Products()
    fun run() {
        outputView.printGreeting()
        getValidInput()
    }

    private fun validateInput(): MutableList<Purchase> {
        return try {
            val userInput = inputView.readItem()
            customer.buy(userInput)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            validateInput()
        }
    }

    private fun getValidInput() {
        while (true) {
            try {
                val purchase = validateInput()
                product.validateInventory(purchase)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}