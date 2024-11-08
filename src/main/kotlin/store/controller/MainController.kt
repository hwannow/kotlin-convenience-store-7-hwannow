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
    fun run() {
        outputView.printGreeting()
        val purchase = getValidInput()

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
}