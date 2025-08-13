package com.faith.products.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.faith.myapplication.models.Product

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch




import android.widget.Toast
import androidx.navigation.NavHostController
import com.faith.myapplication.Data.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ProductViewModel(
    private val navController: NavHostController,
    private val context: Context
) {
    private val supabase = SupabaseClientProvider.supabase

    fun addProduct(name: String, description: String, price: Double, imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bytes = context.contentResolver.openInputStream(imageUri)?.readBytes()
                    ?: throw Exception("Unable to read image")
                val fileName = "${System.currentTimeMillis()}.jpg"

                // Upload image
                supabase.storage.from("product-images").upload(fileName, bytes)

                // Get public URL
                val imageUrl = supabase.storage.from("product-images").publicUrl(fileName)

                // Save product to DB
                supabase.from("products").insert(
                    Product(
                        name = name,
                        description = description,
                        price = price,
                        image_url = imageUrl
                    )
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}



