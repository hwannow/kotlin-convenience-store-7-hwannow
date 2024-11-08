package store.controller

import store.data.Purchase
import store.domain.Customer
import store.domain.Products
import store.view.InputView
import store.view.OutputView

class MainController {
    private val inputView = InputView()
    private val outputView = OutputView(Products().products)
    private val customer = Customer()
    private val product = Products()
    fun run() {
        outputView.printGreeting()
        validateAndDeductInventory()
    }

    private fun getValidInput(): MutableList<Purchase> {
        return try {
            val userInput = inputView.readItem()
            customer.buy(userInput)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            getValidInput()
        }
    }

    private fun validateAndDeductInventory() {
        while (true) {
            try {
                val purchase = getValidInput()
                product.deductFromInventory(purchase)
                break
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}