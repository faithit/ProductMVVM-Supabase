package com.faith.myapplication.models

import kotlinx.serialization.Serializable
@Serializable
data class Product(
    val id:Int?  = null,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val image_url: String = ""
)
