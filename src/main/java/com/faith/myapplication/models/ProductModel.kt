package com.faith.myapplication.models

import kotlinx.serialization.Serializable
@Serializable
data class Product(
    val id: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String
)