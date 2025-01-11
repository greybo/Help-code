package com.example.help_code.presentation.jackson

import kotlinx.coroutines.coroutineScope
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.io.InputStream

class ShopPullParse {
    companion object {
        suspend fun parse(inputStream: InputStream): Shop? {
            return coroutineScope {
                try {
                    parseXml(inputStream)
                } catch (e: Throwable) {
                    Timber.e(e)
                    null
                }
            }

        }

        private fun parseXml(inputStream: InputStream): Shop {
            val parser = XmlPullParserFactory.newInstance().newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            val currencies = mutableListOf<Currency>()
            val categories = mutableListOf<Category>()
            val offers = mutableListOf<Offer>()
            var name = ""
            var company = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "name" -> if (parser.next() == XmlPullParser.TEXT) {
                                name = parser.text
                            }

                            "company" -> if (parser.next() == XmlPullParser.TEXT) {
                                company = parser.text
                            }

                            "currency" -> {
                                val id = parser.getAttributeValue(null, "id")
                                val rate = parser.getAttributeValue(null, "rate").toDouble()
                                currencies.add(Currency(id, rate))
                            }

                            "category" -> {
                                val id = parser.getAttributeValue(null, "id").toInt()
                                val parentId = parser.getAttributeValue(null, "parentId")?.toInt()
                                if (parser.next() == XmlPullParser.TEXT) {
                                    categories.add(Category(id, parentId, parser.text))
                                }
                            }

                            "offer" -> {
                                val offerId = parser.getAttributeValue(null, "id")
                                val groupId = parser.getAttributeValue(null, "group_id")?.toInt()
                                val available =
                                    parser.getAttributeValue(null, "available")?.toBoolean()
                                val inStock =
                                    parser.getAttributeValue(null, "in_stock")?.toBoolean()

                                var price = 0.0
                                var currencyId = ""
                                var name = ""
                                var quantityInStock = 0
                                var categoryId = 0
                                var vendorCode = ""
                                var barcode = ""
                                var description = ""
                                var url = ""
                                val pictures = mutableListOf<String>()
                                val params = mutableMapOf<String, String>()

                                while (parser.next() != XmlPullParser.END_TAG || parser.name != "offer") {
                                    if (parser.eventType != XmlPullParser.START_TAG) {
                                        continue
                                    }
                                    when (parser.name) {
                                        "price" -> if (parser.next() == XmlPullParser.TEXT) {
                                            price = parser.text.toDouble()
                                        }

                                        "currencyId" -> if (parser.next() == XmlPullParser.TEXT) {
                                            currencyId = parser.text
                                        }

                                        "name" -> if (parser.next() == XmlPullParser.TEXT) {
                                            name = parser.text
                                        }

                                        "quantity_in_stock" -> if (parser.next() == XmlPullParser.TEXT) {
                                            quantityInStock = parser.text.toInt()
                                        }

                                        "categoryId" -> if (parser.next() == XmlPullParser.TEXT) {
                                            categoryId = parser.text.toInt()
                                        }

                                        "vendorCode" -> if (parser.next() == XmlPullParser.TEXT) {
                                            vendorCode = parser.text
                                        }

                                        "barcode" -> if (parser.next() == XmlPullParser.TEXT) {
                                            barcode = parser.text
                                        }

                                        "description" -> if (parser.next() == XmlPullParser.TEXT) {
                                            description = parser.text
                                        }

                                        "url" -> if (parser.next() == XmlPullParser.TEXT) {
                                            url = parser.text
                                        }

                                        "picture" -> if (parser.next() == XmlPullParser.TEXT) {
                                            pictures.add(parser.text)
                                        }

                                        "param" -> {
                                            val paramName = parser.getAttributeValue(null, "name")
                                            if (parser.next() == XmlPullParser.TEXT) {
                                                params[paramName] = parser.text
                                            }
                                        }
                                    }
                                    if (parser.eventType == XmlPullParser.END_TAG && parser.name == "offer") break
                                    else if (parser.eventType == XmlPullParser.END_TAG) parser.nextTag()
                                }

                                offers.add(
                                    Offer(
                                        offerId,
                                        groupId,
                                        available,
                                        inStock,
                                        price,
                                        currencyId,
                                        name,
                                        quantityInStock,
                                        categoryId,
                                        vendorCode,
                                        barcode,
                                        description,
                                        url,
                                        pictures,
                                        params
                                    )
                                )
                            }

//                        "offer" -> {
//                            // Example to start parsing offers
//                            // Add similar logic for offers as for currencies and categories
//                            val id = parser.getAttributeValue(null, "id")
//                            val groupId = parser.getAttributeValue(null, "groupId")?.toInt()
//                            val available = parser.getAttributeValue(null, "available").toBoolean()
//                            val price = if (parser.next() == XmlPullParser.TEXT) {
//                                parser.text
//                            } else null
//                            val currencyId: String? = parser.getAttributeValue(null, "currencyId")
//                            val name: String? = parser.getAttributeValue(null, "name")
//                            val quantityInStock: Int? =
//                                parser.getAttributeValue(null, "quantityInStock")?.toInt()
//                            val categoryId: Int? =
//                                parser.getAttributeValue(null, "categoryId")?.toInt()
//                            val vendorCode: String? = parser.getAttributeValue(null, "vendorCode")
//                            val barcode: String? = parser.getAttributeValue(null, "barcode")
//                            val description: String? = parser.getAttributeValue(null, "description")
//                            val url: String? = parser.getAttributeValue(null, "url")
//                            val pictures: List<String>? =
//                                null //parser.getAttributeValue(null, "pictures")
//                            val params: Map<String, String>? =
//                                null // parser.getAttributeValue(null, "params")
//                            offers.add(
//                                Offer(
//                                    id,
//                                    groupId,
//                                    available,
//                                    price,
//                                    currencyId,
//                                    name,
//                                    quantityInStock,
//                                    categoryId,
//                                    vendorCode,
//                                    barcode,
//                                    description,
//                                    url,
//                                    pictures,
//                                    params
//                                )
//                            )
//
//                        }
                        }
                    }
                }
                eventType = parser.next()
            }

            return Shop(
                name = name,
                company = company,
                currencies = currencies,
                categories = categories,
                offers = offers
            )
        }
    }
}