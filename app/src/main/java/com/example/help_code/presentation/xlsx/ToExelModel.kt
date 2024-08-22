package com.example.help_code.presentation.xlsx

import com.google.gson.annotations.SerializedName


class ToExelModel(
    @SerializedName("ID")
    val id: String,
    @SerializedName("SKU")
    val sku: String,
    @SerializedName("Название")
    val name: String,
    @SerializedName("К-во")
    val count: Int,
    @SerializedName("Закупочная цена")
    val price: Double = 0.0,
)
