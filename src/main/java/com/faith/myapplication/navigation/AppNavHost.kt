package com.faith.myapplication.navigation

import AddProductScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faith.myapplication.ui.theme.screens.LOGIN.LoginScreen
import com.faith.myapplication.ui.theme.screens.products.ListProductsScreen
import com.faith.myapplication.ui.theme.screens.register.RegisterScreen
import com.faith.products.ui.HomeScreen

import com.faith.products.viewmodel.ProductViewModel

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
            ListProductsScreen( navController=navController)
        }

    }
}