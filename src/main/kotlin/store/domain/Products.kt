package store.domain

import store.data.Product
import java.io.File

class Products {
    val products = mutableListOf<Product>()
    private var filePath: String = "src/main/resources/products.md"

    init {
        loadProductsFromFile()
        checkProductWithNoPromotion()
    }

    private fun loadProductsFromFile() {
        val file = File(filePath)
        products.clear() // 기존 데이터 초기화
        file.forEachLine { line ->
            if (line.isNotEmpty() && !line.startsWith("name")) {
                val parts = line.split(",")
                products.add(Product(parts[0], parts[1].toInt(), parts[2].toInt(), parts[3]))
            }
        }
    }

    private fun checkProductWithNoPromotion() {
        var index: Int = 0
        while (index < products.size) {
            val productWithPromotion = getProductToBuyWithPromotion(products[index].name)
            if (productWithPromotion == null) {
                index++
                continue
            }
            if (addProductWithNoPromotion(index)) index++
            index++
        }
    }

    private fun addProductWithNoPromotion(index: Int): Boolean {
        val productWithNoPromotion = getProductToBuyWithNoPromotion(products[index].name)
        if (productWithNoPromotion == null) {
            products.add(index + 1, Product(products[index].name, products[index].price, 0, "null"))
            return true
        }
        return false
    }

    fun getProductToBuyWithPromotion(name: String): Product? {
        return products.find { it.name == name && it.promotion != "null" }
    }

    fun getProductToBuyWithNoPromotion(name: String): Product? {
        return products.find { it.name == name && it.promotion == "null" }
    }
}