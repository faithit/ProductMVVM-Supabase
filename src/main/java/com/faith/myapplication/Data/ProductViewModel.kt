package com.faith.products.viewmodel

import android.content.Context
import android.net.Uri

import com.faith.myapplication.models.Product

import kotlinx.coroutines.launch


import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.faith.myapplication.Data.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID


class ProductViewModel(
    private val navController: NavHostController,
    private val context: Context
){


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
    //
    // return all products
    fun allProducts(
        product: MutableState<Product>,
        products: SnapshotStateList<Product>
    ): SnapshotStateList<Product> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = supabase.from("products").select()

                val result = response.decodeList<Product>()

                withContext(Dispatchers.Main) {
                    products.clear()
                    products.addAll(result)
                    if (result.isNotEmpty()) {
                        product.value = result.first()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error loading products: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return products
    }
    //delete a product
    fun deleteProduct(productId:Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabase.from("products").delete {
                    filter {
                        eq("id", productId)
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product deleted!", Toast.LENGTH_SHORT).show()
                    //allProducts() // refresh the product list
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //update a product
    fun updateProduct(
        productId: Long,
        name: String,
        description: String,
        price: Double?,
        imageUri: Uri? = null // optional, only upload if changed
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var imageUrl: String? = null

                // Upload new image if provided
                if (imageUri != null) {
                    val bytes = context.contentResolver.openInputStream(imageUri)?.readBytes()
                        ?: throw Exception("Unable to read image")
                    val fileName = "${UUID.randomUUID()}.jpg"
                    supabase.storage.from("product-images").upload(fileName, bytes)
                    imageUrl = supabase.storage.from("product-images").publicUrl(fileName)
                }

                // Build update data
                val updateData = mutableMapOf<String, Any>(
                    "name" to name,
                    "description" to description,
                    "price" to price!!
                )
                if (imageUrl != null) {
                    updateData["image_url"] = imageUrl
                }

                // ✅ update THART PRODUCT ROW
                supabase.from("products").update(updateData) {
                    filter {
                        eq("id", productId)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product updated!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    //get a single product
    suspend fun getProductById(productId: Long): Product? {
        return try {
            val response = supabase.from("products")
                .select {
                    filter {
                        eq("id", productId)
                    }
                    limit(1)
                }
                .decodeSingleOrNull<Product>()  // ✅ returns null if not found

            response
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error loading product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

}






