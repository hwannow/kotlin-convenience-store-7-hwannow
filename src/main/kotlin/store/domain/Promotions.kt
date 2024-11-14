package store.domain

import store.data.Product
import java.io.File
import store.data.Promotion
import store.data.Purchase
import store.repository.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Promotions() {
    val promotions = mutableListOf<Promotion>()
    var saledItems = mutableListOf<Purchase>()

    init {
        val file = getPromotionFile()
        file.forEachLine { line ->
            if (line.isNotEmpty() && !line.startsWith("name")) {
                val (name, buy, get, startDate, endDate) = line.split(",")
                promotions.add(Promotion(name, buy.toInt(), get.toInt(), parseStringToDate(startDate), parseStringToDate(endDate)))
            }
        }
    }

    private fun getPromotionFile(): File {
        val fileName = "promotions.md"
        val classLoader = Promotion::class.java.classLoader
        return File(classLoader.getResource(fileName)?.toURI())
    }

    private fun parseStringToDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    fun getPromotionToBuy(productToBuy: Product): Promotion {
        val promotionToBuy = promotions.find { it.name == productToBuy.promotion }!!
        return promotionToBuy
    }

    fun addSaledItem(purchase: Purchase) {
        saledItems.add(purchase)
    }

    fun resetPromotion() {
        saledItems = mutableListOf()
    }
}