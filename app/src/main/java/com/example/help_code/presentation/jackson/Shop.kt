package com.example.help_code.presentation.jackson

data class Shop(
    val name: String,
    val company: String,
    val currencies: List<Currency>,
    val categories: List<Category>,
    val offers: List<Offer>
)

data class Currency(
    val id: String,
    val rate: Double
)

data class Category(
    val id: Int,
    val parentId: Int?,
    val name: String
) {
    override fun toString(): String {
        return "Category(id=$id, parentId=$parentId, name='$name')"
    }
}

data class Offer(
    val id: String?,
    val groupId: Int?,
    val available: Boolean?,
    val in_stock: Boolean?,
    val price: Double?,
    val currencyId: String?,
    val name: String?,
    val quantityInStock: Int?,
    val categoryId: Int?,
    val vendorCode: String?,
    val barcode: String?,
    val description: String?,
    val url: String?,
    val pictures: List<String>?,
    val params: Map<String, String>?
) {
    override fun toString(): String {
        return "Offer(id=$id, groupId=$groupId, available=$available, in_stock=$in_stock, price=$price, currencyId=$currencyId, name=$name, quantityInStock=$quantityInStock, categoryId=$categoryId, vendorCode=$vendorCode, barcode=$barcode, description=$description, url=$url, pictures=$pictures, params=$params)"
    }
}
