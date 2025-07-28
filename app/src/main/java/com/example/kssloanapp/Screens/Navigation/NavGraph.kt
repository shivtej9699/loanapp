package com.example.kssloanapp.Screens.Navigation

import SignUpScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kssloanapp.Screens.FromStatus.EMIRepaymentScreen
import com.example.kssloanapp.Screens.FromStatus.LoanStatusScreen
import com.example.kssloanapp.Screens.Home.HomeScreen
import com.example.kssloanapp.Screens.Login.LoginScreen
import com.example.kssloanapp.Screens.Loan.*
import com.example.kssloanapp.Screens.Profile.ProfileScreen
import com.example.kssloanapp.Screens.Start.WelcomeScreen
import com.example.kssloanapp.Screens.Splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {

    val user = FirebaseAuth.getInstance().currentUser
    val start = if(user != null) "Bottom" else "login"


    NavHost(
        navController = navController,
        startDestination = start
    ) {
//        composable("StartScreen") {
//            StartScreen(navController)
//        }

            composable("SplashScreen") {
                SplashScreen(navController)
            }

        composable("WelcomeScreen") {
            WelcomeScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("SignupScreen") {
            SignUpScreen(navController)
        }

        composable("Bottom") {
            BottomNav(navController)
        }

        composable("Home") {
            HomeScreen(navController)
        }

        composable("Profile") {
            ProfileScreen(navController)
        }

        // SelectLoanScreen with bankName passed
        composable("SelectLoanScreen/{bankName}") { backStackEntry ->
            val bankName = backStackEntry.arguments?.getString("bankName") ?: "Unknown Bank"
            SelectLoanScreen(navController = navController, bankName = bankName)
        }

        // Loan Form screen with dynamic loanType
        composable(
            "LoanFormScreen/{loanType}",
            arguments = listOf(navArgument("loanType") { type = NavType.StringType })
        ) { backStackEntry ->
            val loanType = backStackEntry.arguments?.getString("loanType") ?: "Loan"
            LoanFormScreen(loanType = loanType, navController = navController)
        }

        // ✅ Corrected: Loan Status screen with loanId
        composable(
            route = "loan_status/{loanId}",
            arguments = listOf(navArgument("loanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
            LoanStatusScreen(loanId = loanId, navController = navController)
        }

        composable(
            route = "emi_repayment/{loanId}",
            arguments = listOf(navArgument("loanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
            EMIRepaymentScreen(loanId = loanId, navController = navController)
        }


    }
}