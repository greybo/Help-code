package com.example.help_code.presentation.jackson

import android.util.Xml
import androidx.lifecycle.ViewModel
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class PullParserViewModel:ViewModel() {
    fun parseProductsXml(xml: InputStream): List<Product> {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(xml, null)
        parser.nextTag()
        return readProducts(parser)
    }

    private fun readProducts(parser: XmlPullParser): List<Product> {
        val products = mutableListOf<Product>()

        parser.require(XmlPullParser.START_TAG, null, "Products")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "Product") {
                products.add(readProduct(parser))
            } else {
                skip(parser)
            }
        }
        return products
    }

    private fun readProduct(parser: XmlPullParser): Product {
        parser.require(XmlPullParser.START_TAG, null, "Product")
        var id: Int = 0
        var name: String = ""
        var price: Int = 0

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "Id" -> id = readInt(parser, "Id")
                "Name" -> name = readString(parser, "Name")
                "Price" -> price = readInt(parser, "Price")
                else -> skip(parser)
            }
        }
        return Product(id, name, price)
    }

    private fun readString(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val result = if (parser.next() == XmlPullParser.TEXT) parser.text else ""
        parser.nextTag()
        return result
    }

    private fun readInt(parser: XmlPullParser, tag: String): Int {
        return readString(parser, tag).toInt()
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}


data class Product(
    val id: Int,
    val name: String,
    val price: Int

) {
    override fun toString(): String {
        return "Product(id=$id, name='$name', price=$price)"
    }
}
