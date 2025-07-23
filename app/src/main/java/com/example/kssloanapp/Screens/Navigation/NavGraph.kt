package com.example.kssloanapp.Screens.Navigation


<<<<<<< HEAD
=======
import HomeScreen
>>>>>>> a3c114c (first)
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
<<<<<<< HEAD
import com.example.kssloanapp.Screens.Home.HomeScreen
import com.example.kssloanapp.Screens.Login.LoginScreen
import com.example.kssloanapp.Screens.Login.WelcomeScreen
import com.example.kssloanapp.Screens.Profile.ProfileScreen
=======
import com.example.kssloanapp.Screens.Home.LoanApplicationForm
import com.example.kssloanapp.Screens.Home.ProfileScreen
import com.example.kssloanapp.Screens.Login.LoginScreen
import com.example.kssloanapp.Screens.Login.WelcomeScreen
>>>>>>> a3c114c (first)
////import androidx.navigation.navArgument
////import com.example.ecomm.Screens.CartSystem.CartScreen
////import com.example.ecomm.Screens.Home.HomeScreen
//import com.example.ecomm.Screens.LoginIn.LoginScreen
//import com.example.ecomm.Screens.Product.ProductDetailScreen
//import com.example.ecomm.Screens.Profile.ProfileScreen
//import com.example.ecomm.Screens.SignIN.SignInScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController) {


    val user = FirebaseAuth.getInstance().currentUser
//
//    val start = if (user != null) "bottomNav" else "login"

    NavHost(
        navController = navController, startDestination = "WelcomeScreen"

    ) {
        composable("WelcomeScreen") {
            WelcomeScreen(navController)
        }

        composable("login")
        {
            LoginScreen(navController)
        }
        composable("SignupScreen"){
            SignUpScreen(navController)
        }
        composable("Bottom"){
            BottomNav(navController)
        }
        composable("Home"){
            HomeScreen(navController)
        }
        composable("Profile"){
            ProfileScreen(navController)
        }
<<<<<<< HEAD
=======
        composable("LoanScreen"){
            LoanApplicationForm(navController)
        }
>>>>>>> a3c114c (first)
//        composable("cart"){
//            CartScreen()
//        }
//
//        composable("product/{name}/{description}/{price}",
//            arguments = listOf(
//                navArgument("name") {type = NavType.StringType},
//                navArgument("description") {type = NavType.StringType},
//                navArgument("price") {type = NavType.StringType}
//
//            )
//        ){ navBackStackEntry ->
//            ProductDetailScreen(
//                navController,
//                productName = navBackStackEntry.arguments?.getString("name") ?: "",
//                productDescription = navBackStackEntry.arguments?.getString("description") ?: "",
//                productPrice = navBackStackEntry.arguments?.getString("price") ?: ""
//
//            )


        //composable("searchBar"){
        //   HomeScreen(navController)
        //}


    }
}