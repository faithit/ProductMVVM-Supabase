package com.faith.myapplication.ui.theme.screens.products

import AddProductScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.faith.myapplication.models.Product
import com.faith.products.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProductsScreen(navController: NavHostController) {
    val product = remember { mutableStateOf(Product(0, "", "", 0.0,"")) }
    val products = remember { mutableStateListOf<Product>() }
    val context = LocalContext.current
    val productviewmodel=  ProductViewModel(navController, context)
    // Load products from Supabase
    LaunchedEffect(Unit) {
        productviewmodel.allProducts(product, products)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product List") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            items(products) { prod ->
                ProductItem(
                    product = prod,
                    onEdit = {
                        // Navigate to your EditProductScreen with the product ID
                        //navController.navigate("edit_product/${prod.id}")
                    },
                    onDelete = {
                        //productviewmodel.deleteProduct(prod.id ?: "")
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image_url),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(product.name ?: "", style = MaterialTheme.typography.titleMedium)
                Text(product.description ?: "", style = MaterialTheme.typography.bodyMedium)
                Text("Ksh ${product.price ?: 0.0}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun listpreview(){
 ListProductsScreen(rememberNavController())
}