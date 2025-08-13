import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.faith.myapplication.R

import com.faith.products.viewmodel.ProductViewModel


@Composable
fun AddProductScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = ProductViewModel(navController, context)

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    //variable to store picked image uri
    var imageUri by remember { mutableStateOf<Uri?>(null) }
//image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        Text(
            text = "Add PRODUCT",
            color = Color.Magenta,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") })
        Card(
            shape= CircleShape,
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(140.dp)
                .clickable{}
                .shadow(8.dp,CircleShape)
        )
        {
            AsyncImage(
                model=imageUri ?:R.drawable.product,
                contentDescription = "product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(140.dp)
            )

        }

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Pick Image")
        }

        Button(
            onClick = {
                imageUri?.let {
                    viewModel.addProduct(name, description, price.toDoubleOrNull() ?: 0.0, it)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Product")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun addpreview(){
    AddProductScreen(rememberNavController())
}


