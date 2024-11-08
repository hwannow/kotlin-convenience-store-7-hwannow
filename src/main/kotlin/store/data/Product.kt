package store.data

data class Product(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: String?
)
