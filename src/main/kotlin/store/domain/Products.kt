package store.domain

import store.data.Product
import store.data.Purchase
import java.io.File

class Products {
    val products = mutableListOf<Product>()
    private var filePath: String = "src/main/resources/products.md"

    init {
        loadProductsFromFile()
        checkPromotion()
    }

    private fun loadProductsFromFile() {
        val file = File(filePath)
        products.clear() // 기존 데이터 초기화
        file.forEachLine { line ->
            if (line.isNotEmpty() && !line.startsWith("name")) {
                val parts = line.split(",")
                val name = parts[0]
                val price = parts[1].toInt()
                val quantity = parts[2].toInt()
                val promotion = parts[3]

                products.add(Product(name, price, quantity, promotion))
            }
        }
    }

    fun updateInventoryFile() {
        val file = File(filePath)

        file.printWriter().use { writer ->
            writer.println("name,price,quantity,promotion")
            products.forEach { product ->
                writer.println("${product.name},${product.price},${product.quantity},${product.promotion}")
            }
        }
    }

    private fun checkPromotion() {
        var index: Int = 0
        while (index < products.size) {
            val productWithPromotion = products.find { it.name == products[index].name && it.promotion != "null" }
            if (productWithPromotion == null) {
                index++
                continue
            }
            val productWithNoPromotion = getProductToBuyWithNoPromotion(products[index].name)
            if (productWithNoPromotion == null) {
                products.add(index + 1, Product(products[index].name, products[index].price, 0, "null"))
                index++
            }
            index++
        }
    }

    fun getProductToBuyWithPromotion(purchase: Purchase): Product? {
        return products.find { it.name == purchase.productName && it.promotion != "null" }
    }

    fun getProductToBuyWithNoPromotion(name: String): Product? {
        return products.find { it.name == name && it.promotion == "null" }
    }
}