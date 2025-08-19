import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.faith.myapplication.models.Product
import com.faith.products.viewmodel.ProductViewModel

@Composable
fun UpdateProductScreen(
    navController: NavHostController,
    productId: Long,
) {
    val context = LocalContext.current
    val productViewModel = ProductViewModel(navController, context)

    // Get product from ViewModel
    val product by produceState<Product?>(initialValue = null, productId) {
        value = productViewModel.getProductById(productId) // fetch from supabase/db
    }

    if (product == null) {
        Text("Loading...")
    } else {
        // Prefilled values
        var name by remember { mutableStateOf(product!!.name) }
        var description by remember { mutableStateOf(product!!.description) }
        var price by remember { mutableStateOf(product!!.price.toString()) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        // Image picker launcher
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Update Product", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show current or new image
//            Image(
//                painter = rememberAsyncImagePainter(imageUri ?: product.image_url),
//                contentDescription = "Product Image",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentScale = ContentScale.Crop
//            )
            val imageSource: Any = imageUri ?: product!!.image_url

            Image(
                painter = rememberAsyncImagePainter(model = imageSource),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                imagePickerLauncher.launch("image/*")
            }) {
                Text("Change Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                    productViewModel.updateProduct(
                        productId = productId,
                        name = name,
                        description = description,
                        price = price.toDoubleOrNull() ?: 0.0,
                        imageUri = imageUri)
            }) {
                Text("Update Product")
            }
        }
    }

}