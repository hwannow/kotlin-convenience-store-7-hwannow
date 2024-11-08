package store.domain

import store.data.Product
import java.io.File

class Products {
    val products = mutableListOf<Product>()
    init {
        val fileName = "products.md"
        val classLoader = Product::class.java.classLoader
        val file = File(classLoader.getResource(fileName)?.toURI())

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
}