package com.faith.myapplication.navigation

import AddProductScreen
import UpdateProductScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faith.myapplication.ui.theme.screens.Login.LoginScreen
import com.faith.myapplication.ui.theme.screens.products.DisplayProductsScreen

import com.faith.myapplication.ui.theme.screens.register.RegisterScreen
import com.faith.products.ui.HomeScreen

@Composable
fun  AppNavHost(
    modifier: Modifier=Modifier,
    navController: NavHostController= rememberNavController(),

    startDestination:String=ROUTE_HOME
){

    NavHost(
        navController=navController,
        modifier=modifier,
        startDestination=startDestination)
    {
        composable(ROUTE_LOGIN) {
            LoginScreen(navController=navController)
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(navController=navController)
        }
        composable(ROUTE_HOME) {
            HomeScreen(navController=navController)
        }

        composable(ROUTE_ADDPRODUCT) {
            AddProductScreen(navController=navController)
        }
        composable(ROUTE_LISTPRODUCTS) {
            DisplayProductsScreen( navController=navController)
        }
        composable(ROUTE_UPDATEPRODUCT+"/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")!!.toLong()
            UpdateProductScreen(navController, productId)
        }

    }
}