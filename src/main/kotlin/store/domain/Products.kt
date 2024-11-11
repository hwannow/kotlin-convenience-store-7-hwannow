package store.domain

import store.data.Product
import store.data.Purchase
import java.io.File

class Products {
    val products = mutableListOf<Product>()
    private var filePath: String = "src/main/resources/products.md"

    init {
        val resetFile = File("src/main/resources/reset.md")
        val originalFile = File("src/main/resources/products.md")
        resetFile.copyTo(originalFile, overwrite = true)

        loadProductsFromFile(File(filePath))
    }

    fun loadProductsFromFile(file: File) {
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

    fun getProductToBuyWithPromotion(purchase: Purchase): Product? {
        return products.find { it.name == purchase.productName && it.promotion != "null" }
    }

    fun getProductToBuyWithNoPromotion(name: String): Product? {
        return products.find { it.name == name && it.promotion == "null" }
    }
}