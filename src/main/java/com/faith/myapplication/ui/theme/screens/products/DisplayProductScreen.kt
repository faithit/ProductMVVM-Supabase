package com.faith.myapplication.ui.theme.screens.products




import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.faith.myapplication.navigation.ROUTE_UPDATEPRODUCT
import com.faith.products.viewmodel.ProductViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayProductsScreen(navController: NavHostController) {
    val product = remember { mutableStateOf(Product(0, "", "", 0.0, "")) }
    val products = remember { mutableStateListOf<Product>() }
    val context = LocalContext.current
    val productviewmodel = ProductViewModel(navController, context)

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
                        //  navigate to editproduct
                        navController.navigate("$ROUTE_UPDATEPRODUCT/${prod.id}")
                    },
                    onDelete = {
                        productviewmodel.deleteProduct(prod.id !!)
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
        Column(modifier = Modifier.padding(16.dp)) {

            // Product Image
            Image(
                painter = rememberAsyncImagePainter(product.image_url),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )

            // Product Info
            Text(product.name ?: "", style = MaterialTheme.typography.titleMedium)
            Text(product.description ?: "", style = MaterialTheme.typography.bodyMedium)
            Text("Ksh ${product.price ?: 0.0}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Edit",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onEdit() }
                )
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun productsdisplaypreview() {
    DisplayProductsScreen(rememberNavController())
}
